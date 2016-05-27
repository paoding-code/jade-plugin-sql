/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.standard;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IColumnMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IEntityMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IExpandableParameterMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IParameterMapper;
import net.paoding.rose.jade.plugin.sql.util.PlumUtils;
import net.paoding.rose.jade.statement.StatementRuntime;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class UpdateGenerator extends ConditionalGenerator {
	
	@Override
	protected StringBuilder beforeApplyConditions(
			ConditionalOperationMapper operationMapper,
			StatementRuntime runtime, StringBuilder sql) {
		sql = super.beforeApplyConditions(operationMapper, runtime, sql);
		
		sql.append("UPDATE ");
		sql.append(operationMapper.getTargetEntityMapper().getDestName());
		
		return sql;
	}
	
	@Override
	public StringBuilder applyConditions(ConditionalOperationMapper operationMapper,
			StatementRuntime runtime, StringBuilder sql) {
		if(operationMapper.isEntityMode()
				|| operationMapper.isEntityCollectionMode()) {
			// 通过实体或实体集合更新
			
			Map<String, Object> parametersValue = runtime.getParameters();
			if(!operationMapper.getDestName().equals(IOperationMapper.OPERATION_UPDATE)) {
				throw new InvalidDataAccessApiUsageException("Operation mapper must be a update.");
			}
			
			List<IParameterMapper> parameters = operationMapper.getParameters();
			if(PlumUtils.isEmpty(parameters)) {
				throw new InvalidDataAccessApiUsageException("Update operation must have parameters.");
			}
			
			IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
			if(parameters.size() == 1
					&& parameters.get(0).getType().equals(targetEntityMapper.getOriginal())) {
				StringBuilder where = new StringBuilder(32);
				IParameterMapper param = parameters.get(0);
				
				if(param instanceof IExpandableParameterMapper) {
					List<IParameterMapper> expandParams = ((IExpandableParameterMapper) param).expand();
					Object parameterValue = parametersValue.get(":1");
					
					sql.append(" SET ");
						for(IParameterMapper p : expandParams) {
							IColumnMapper columnMapper = p.getColumnMapper();
							if(columnMapper != null) {
								Field field = p.getColumnMapper().getOriginal();
								Object value = null;
								try {
									value = field.get(parameterValue);
								} catch (Exception e) {
									throw new IllegalArgumentException("Cannot get field value \"" + operationMapper.getTargetEntityMapper().getOriginal().getSimpleName() + "." + field.getName() + "\".", e);
								}
								if(p.getColumnMapper().isPrimaryKey()) {
									// 主键视为条件
									if(value == null) {
										throw new IllegalArgumentException("Cannot execute update, primary key \"" + p.getColumnMapper().getDestName() + "\" must not be null.");
									}
									
									if(where.length() > 0) {
										where.append(" AND ");
									} else {
										where.append(" WHERE ");
									}
									
									where.append(p.getColumnMapper().getDestName());
									where.append(" = :1.");
									where.append(p.getColumnMapper().getOriginalName());
								} else {
									if(param.isIgnoreNull()
											&& value == null) {
										continue;
									} else {
										sql.append(p.getColumnMapper().getDestName());
										sql.append(" = ");
										sql.append(":1.");
										sql.append(p.getColumnMapper().getOriginalName());
										sql.append(",");
									}
								}
							}
						}
						sql.setLength(sql.length() - 1);
						sql.append(where);

				} else {
					throw new InvalidDataAccessApiUsageException("Auto update operation parameter must be a expandable.");
				}
				
			} else {
				throw new InvalidDataAccessApiUsageException("Please use the entity object to update.");
			}
		} else {
			int start = sql.length();
			List<IParameterMapper> parameters = operationMapper.getParameters();
			
			for(int i = 0; i < operationMapper.getWhereAt(); i++) {
				IParameterMapper param = parameters.get(i);
				boolean ignoreNull = operationMapper.isIgnoreNull() || param.isIgnoreNull();
				Object value = runtime.getParameters().get(param.getOriginalName());
				
				if(ignoreNull
						&& value == null) {
					continue;
				}
				if(sql.length() == start) {
					sql.append(" SET ");
				}
				sql.append(param.getDestName());
				sql.append(" = :");
				sql.append(param.getOriginalName());
				sql.append(",");
			}
			
			if(sql.length() == start) {
				throw new InvalidDataAccessApiUsageException("Update is ignore null, must have least 1 non-null parameter.");
			}
			
			if(sql.charAt(sql.length() - 1) == ',') {
				sql.setLength(sql.length() - 1);
			}
			sql = super.applyConditions(operationMapper, runtime, sql);
		}
		
		return sql;
	}
	
}
