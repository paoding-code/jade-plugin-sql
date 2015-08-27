/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.mysql;

import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class DeleteGenerator extends ConditionalGenerator {

	@Override
	protected void beforeApplyConditions(
			ConditionalOperationMapper operationMapper,
			StatementRuntime runtime, StringBuilder sql) {
		super.beforeApplyConditions(operationMapper, runtime, sql);
		
		sql.append("DELETE FROM ");
		sql.append(operationMapper.getTargetEntityMapper().getName());
	}

}
