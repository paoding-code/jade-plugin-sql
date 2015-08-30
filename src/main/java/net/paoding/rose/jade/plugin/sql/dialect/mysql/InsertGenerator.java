/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.mysql;

import java.util.List;

import net.paoding.rose.jade.plugin.sql.dialect.ISQLGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.IEntityMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IExpandableParameterMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IParameterMapper;
import net.paoding.rose.jade.plugin.sql.mapper.OperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

import org.springframework.util.CollectionUtils;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class InsertGenerator implements ISQLGenerator<OperationMapper> {

	/* (non-Javadoc)
	 * @see com.cainiao.depot.project.biz.common.jade.dialect.ISQLGenerator#generate(com.cainiao.depot.project.biz.common.jade.mapper.IOperationMapper)
	 */
	@Override
	public String generate(OperationMapper operationMapper, StatementRuntime runtime) {
		if(!operationMapper.getName().equals(IOperationMapper.OPERATION_INSERT)) {
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
			StringBuilder sql = new StringBuilder("INSERT INTO ");
			StringBuilder values = new StringBuilder();
			sql.append(targetEntityMapper.getName());
			sql.append("(");
			
			for(IParameterMapper param : expandParams) {
				sql.append(param.getColumnMapper().getName());
				sql.append(",");
				
				values.append(":");
				values.append(entityParam.getName());
				values.append(".");
				values.append(param.getColumnMapper().getOriginalName());
				values.append(",");
			}
			sql.setLength(sql.length() - 1);
			values.setLength(values.length() - 1);
			
			sql.append(")VALUES(");
			sql.append(values.toString());
			sql.append(")");
			
			return sql.toString();
		} else {
			throw new IllegalArgumentException("Parameter \"" + entityParam.getOriginalName() + "\" cannot expend.");
		}
	}

}
