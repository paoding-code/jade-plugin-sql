/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.standard;

import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.CollectionUtils;

import net.paoding.rose.jade.plugin.sql.dialect.ISQLGenerator;
import net.paoding.rose.jade.plugin.sql.id.CachedIDGeneratorManager;
import net.paoding.rose.jade.plugin.sql.id.IDGenerator;
import net.paoding.rose.jade.plugin.sql.id.IDGeneratorManager;
import net.paoding.rose.jade.plugin.sql.id.ManualGenerator;
import net.paoding.rose.jade.plugin.sql.id.NativeGenerator;
import net.paoding.rose.jade.plugin.sql.id.NonID;
import net.paoding.rose.jade.plugin.sql.mapper.IColumnMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IEntityMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IExpandableParameterMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IParameterMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class InsertGenerator implements ISQLGenerator {
	
	private IDGeneratorManager idGeneratorManager = new CachedIDGeneratorManager();

	/* (non-Javadoc)
	 * @see com.cainiao.depot.project.biz.common.jade.dialect.ISQLGenerator#generate(com.cainiao.depot.project.biz.common.jade.mapper.IOperationMapper)
	 */
	@Override
	public String generate(IOperationMapper operationMapper, StatementRuntime runtime) {
        if(!operationMapper.getDestName().equals(IOperationMapper.OPERATION_INSERT)) {
			throw new IllegalArgumentException("Operation mapper must be a insert.");
		}
		
		List<IParameterMapper> parameters = operationMapper.getParameters();
		
		if(CollectionUtils.isEmpty(parameters)) {
			throw new IllegalArgumentException("Update operation must have parameters.");
		}
		
		IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
		
		IParameterMapper entityParam = parameters.get(0);
		
		if(entityParam instanceof IExpandableParameterMapper) {
			List<IParameterMapper> expandParams = ((IExpandableParameterMapper) entityParam).expand();
			StringBuilder sql = new StringBuilder(500);
            StringBuilder values = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(targetEntityMapper.getDestName());
			sql.append("(");
			
			for(IParameterMapper param : expandParams) {
				IColumnMapper columnMapper = param.getColumnMapper();
				if(columnMapper.isPrimaryKey()) {
					Class<? extends IDGenerator<?>> idGeneratorType = columnMapper.getIDGeneratorType();
					
					if(idGeneratorType == NativeGenerator.class
							// 兼容pk属性
							|| idGeneratorType == NonID.class) {
						// 数据库自增型ID在插入时不做任何处理，也不参与插入字段。
						continue;
					} else if(idGeneratorType != ManualGenerator.class){
						// 非手动设置ID，则需要生成值。
						IDGenerator<?> idGenerator = idGeneratorManager.get(idGeneratorType);
						if(idGenerator != null) {
							Object entity = runtime.getParameters().get(":1");
							Object idValue = idGenerator.generate(entity);
							
							try {
								columnMapper.getOriginal().set(entity, idValue);
							} catch (Exception e) {
								throw new InvalidDataAccessApiUsageException("Cannot set id to entity property.");
							}
						}
					}
				}
				
				sql.append(param.getColumnMapper().getDestName());
				sql.append(",");
				
				values.append(":1.");
				values.append(param.getColumnMapper().getOriginalName());
				values.append(",");
			}
			sql.setLength(sql.length() - 1);
			values.setLength(values.length() - 1);
			
			sql.append(")VALUES(");
			sql.append(values);
			sql.append(")");
			
			return sql.toString();
		} else {
			throw new IllegalArgumentException("Parameter \"" + entityParam.getOriginalName() + "\" cannot expend.");
		}
	}

}
