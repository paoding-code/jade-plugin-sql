/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.standard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.plugin.sql.Order;
import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.Order.Group;
import net.paoding.rose.jade.plugin.sql.dialect.standard.ConditionalGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IColumnMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IEntityMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public abstract class AbstractSelectGenerator extends ConditionalGenerator {
	
	private static final Map<Direction, String> DIRECTIONS;
	
	static {
		Map<Direction, String> directions = new HashMap<Order.Direction, String>(2);
		directions.put(Direction.ASC, "ASC");
		directions.put(Direction.DESC, "DESC");
		
		DIRECTIONS = Collections.unmodifiableMap(directions);
	}
	
	@Override
	protected StringBuilder beforeApplyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		super.beforeApplyConditions(operationMapper, runtime, sql);
		IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
		sql.append(operationMapper.getName());
		sql.append(" ");
		
		applyColumns(operationMapper, runtime, sql);
		
		sql.append(" FROM ");
		sql.append(targetEntityMapper.getName());
		
		return sql;
	}
	
	protected void applyColumns(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
		if(operationMapper.isCountQuery()) {
			sql.append("COUNT(*)");
		} else {
			applyColumns(targetEntityMapper, sql);
		}
	}
	
	protected void applyColumns(IEntityMapper targetEntityMapper, StringBuilder sql) {
		List<IColumnMapper> columns = targetEntityMapper.getColumns();
		
		for(IColumnMapper col : columns) {
			String name = col.getName();
			sql.append(name);
			sql.append(" ");
			sql.append(col.getOriginal().getName());
			sql.append(",");
		}
		
		sql.setLength(sql.length() - 1);
	}

	@Override
	protected StringBuilder afterApplyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		super.afterApplyConditions(operationMapper, runtime, sql);
		
		if(!operationMapper.isCountQuery()) {
			// 节省数据库性能开销，count操作不需要orderBy。
			sql = applyOrderBy(operationMapper, runtime, sql);
		}
		
		Map<String, Object> parameters = runtime.getParameters();
		
		Long offset = null;
		Long limit = null;
		
		if(operationMapper.containsOffset()) {
			Number val = (Number) parameters.get(":" + (operationMapper.getOffsetParameterIndex() + 1));
			if(val != null) {
				offset = val.longValue();
			}
		}
		
		if(operationMapper.containsLimit()) {
			Number val = (Number) parameters.get(":" + (operationMapper.getLimitParameterIndex() + 1));
			if(val != null) {
				limit = val.longValue();
			}
		}
		
		if(limit != null || offset != null) {
			sql = applyRange(operationMapper, runtime, sql, offset, limit);
		}
		
		return sql;
	}
	
	protected StringBuilder applyOrderBy(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		if(operationMapper.containsOrder()
				&& !operationMapper.isPrimaryKeyMode()) {
			IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
			
			Order order = null;
			Integer orderParameterIndex = operationMapper.getOrderParameterIndex();
			
			if(orderParameterIndex == -1) {
				order = targetEntityMapper.getDefaultOrder();
			} else {
				Object value = runtime.getParameters().get(":" + (operationMapper.getOrderParameterIndex() + 1));
				order = (Order) value;
			}
			
			sql.append(" ORDER BY ");
			
			List<Group> groups = order.getGroups();
			
			if(groups.size() > 0) {
				for(Group group : groups) {
					String[] fields = group.getFields();
					
					for(String field : fields) {
						IColumnMapper col = targetEntityMapper.getColumnMapperByFieldName(field);
						
						if(col != null) {
							
							sql.append(col.getName());
							sql.append(" ");
							sql.append(DIRECTIONS.get(group.getDirection()));
							sql.append(",");
						} else {
							throw new IllegalArgumentException("Cannot find column by field name \"" + field + "\".");
						}
					}
				}
				
				sql.setLength(sql.length() - 1);
			}
		}
		
		return sql;
	}
	
	protected abstract StringBuilder applyRange(ConditionalOperationMapper operationMapper,
			StatementRuntime runtime,
			StringBuilder sql,
			Long offset,
			Long limit);
}
