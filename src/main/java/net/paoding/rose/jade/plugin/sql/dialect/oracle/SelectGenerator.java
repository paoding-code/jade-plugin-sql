/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.oracle;

import java.util.Map;

import net.paoding.rose.jade.plugin.sql.dialect.standard.AbstractSelectGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IColumnMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan
 *
 */
public class SelectGenerator extends AbstractSelectGenerator {
	
	protected static final String ROWNUM_ALIAS = "RN";

	/* (non-Javadoc)
	 * @see net.paoding.rose.jade.plugin.sql.dialect.standard.AbstractSelectGenerator#applyRange(net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper, net.paoding.rose.jade.statement.StatementRuntime, java.lang.StringBuilder, java.lang.Number, java.lang.Number)
	 */
	@Override
	protected StringBuilder applyRange(ConditionalOperationMapper operationMapper,
			StatementRuntime runtime, StringBuilder sql, Long offset,
			Long limit) {
		
		Map<String, Object> parameters = runtime.getParameters();
		
		// 在Oracle数据库版本12g之前，limit与offset通过ROWNUM计算，且offset必须使用子查询。
		Long realOffset = null;
		
		if(offset == null) {
			offset = 0L;
		}
		
		if(offset != null) {
			realOffset = offset + 1;
		}
		
		if(limit != null && limit > 0) {
			if(containsConditions()) {
				sql.append(" AND ");
			}
			sql.append(" ROWNUM < :__limit");
			parameters.put("__limit", realOffset + limit);
		}
		
		if(offset != null && offset > 1) {
			StringBuilder outerSql = new StringBuilder(operationMapper.getDestName());
			outerSql.append(" ");
			applyColumns(operationMapper.getTargetEntityMapper(), outerSql);
			
			outerSql.append(" FROM (");
			outerSql.append(sql);
			outerSql.append(") WHERE ");
			outerSql.append(ROWNUM_ALIAS);
			outerSql.append(" >= :__offset");
			
			parameters.put("__offset", realOffset);
			
			sql = outerSql;
		}
		
		return sql;
	}
	
	@Override
	protected void applyColumns(ConditionalOperationMapper operationMapper, StatementRuntime runtime,
			StringBuilder sql) {
		super.applyColumns(operationMapper, runtime, sql);
		
		if(!operationMapper.isCountQuery()
				&& operationMapper.containsOffset()) {
			Number val = (Number) runtime.getParameters().get(":" + (operationMapper.getOffsetParameterIndex() + 1));
			if(val != null
					&& val.longValue() > 1) {
				// 提供给外部查询做偏移量判定
				sql.append(", ROWNUM ");
				sql.append(ROWNUM_ALIAS);
			}
		}
	}
	
	@Override
	protected String getColumnAlias(IColumnMapper column) {
		if(column.getDestName().startsWith("\"")
				&& column.getDestName().endsWith("\"")) {
			return "\"" + super.getColumnAlias(column) + "\"";
		}
		return super.getColumnAlias(column);
	}

}
