/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect;

import net.paoding.rose.jade.plugin.sql.dialect.mysql.SelectGenerator;
import net.paoding.rose.jade.plugin.sql.dialect.standard.DeleteGenerator;
import net.paoding.rose.jade.plugin.sql.dialect.standard.InsertGenerator;
import net.paoding.rose.jade.plugin.sql.dialect.standard.UpdateGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * SQL语句生成器<p>
 * 
 * 以 MySQL 的实现为例，SQL语句分为2大类，第一大类是条件语句，第二类是插入语句。其中条件语句详细分为查询语句、更新语句、删除语句。
 * 
 * @see SelectGenerator
 * @see UpdateGenerator
 * @see DeleteGenerator
 * @see InsertGenerator
 * 
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
public interface ISQLGenerator {

	/**
	 * 将某种操作生成SQL
	 * @param operationMapper
	 * @param runtime
	 * @return
	 */
	String generate(IOperationMapper operationMapper, StatementRuntime runtime);
	
}
