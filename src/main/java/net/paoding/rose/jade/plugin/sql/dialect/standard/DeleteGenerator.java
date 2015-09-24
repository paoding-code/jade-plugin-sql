/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.standard;

import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class DeleteGenerator extends ConditionalGenerator {

	@Override
	protected StringBuilder beforeApplyConditions(
			ConditionalOperationMapper operationMapper,
			StatementRuntime runtime, StringBuilder sql) {
		sql = super.beforeApplyConditions(operationMapper, runtime, sql);
		
		sql.append("DELETE FROM ");
		sql.append(operationMapper.getTargetEntityMapper().getName());
		
		return sql;
	}

}
