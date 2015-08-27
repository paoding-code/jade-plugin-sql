/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect;

import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * 查询语句生成器
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
public interface ISQLGenerator<T extends IOperationMapper> {

	/**
	 * 将某种操作生成SQL
	 * @param operationMapper
	 * @param runtime
	 * @return
	 */
	String generate(T operationMapper, StatementRuntime runtime);
	
}
