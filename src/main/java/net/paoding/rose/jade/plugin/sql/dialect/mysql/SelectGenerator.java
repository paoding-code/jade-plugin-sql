/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.mysql;

import java.util.Map;

import net.paoding.rose.jade.plugin.sql.dialect.standard.AbstractSelectGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class SelectGenerator extends AbstractSelectGenerator {
	
	protected StringBuilder applyRange(ConditionalOperationMapper operationMapper,
			StatementRuntime runtime,
			StringBuilder sql,
			Long offset,
			Long limit) {
		Map<String, Object> parameters = runtime.getParameters();
		
		sql.append(" LIMIT :__offset, :__limit");
		
		if(limit != null && offset != null) {
			parameters.put("__offset", offset);
			parameters.put("__limit", limit);
		} else if(offset != null) {
			parameters.put("__offset", offset);
			parameters.put("__limit", -1);
		} else if(limit != null && limit >= 0) {
			parameters.put("__offset", 0);
			parameters.put("__limit", limit);
		}
		
		return sql;
	}
}
