/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect;

import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * 数据库方言抽象接口
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
public interface IDialect {

	/**
	 * 将指定的操作转换为目标数据库的查询语句
	 * @param operation 操作映射对象
	 * @param runtime Statement运行时
	 * 
	 * @return 查询语句
	 * 
	 * @see IOperationMapper
	 * @see StatementRuntime
	 */
	public <T extends IOperationMapper> String translate(T operation, StatementRuntime runtime);
}
