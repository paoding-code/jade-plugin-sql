/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect;

import java.util.HashMap;
import java.util.Map;

import net.paoding.rose.jade.plugin.sql.dialect.mysql.SelectGenerator;
import net.paoding.rose.jade.plugin.sql.dialect.standard.DeleteGenerator;
import net.paoding.rose.jade.plugin.sql.dialect.standard.InsertGenerator;
import net.paoding.rose.jade.plugin.sql.dialect.standard.UpdateGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * MySQL方言
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
public class MySQLDialect implements IDialect {
	
	private Map<String, ISQLGenerator> generators;
	
	public MySQLDialect() {
		generators = new HashMap<String, ISQLGenerator>();
		generators.put(IOperationMapper.OPERATION_SELECT, new SelectGenerator());
		generators.put(IOperationMapper.OPERATION_INSERT, new InsertGenerator());
		generators.put(IOperationMapper.OPERATION_UPDATE, new UpdateGenerator());
		generators.put(IOperationMapper.OPERATION_DELETE, new DeleteGenerator());
	}

	/* (non-Javadoc)
	 * @see com.cainiao.depot.project.biz.common.jade.dialect.IDialect#translate(com.cainiao.depot.project.biz.common.jade.mapper.IOperationMapper)
	 */
	@Override
	public String translate(IOperationMapper operation, StatementRuntime runtime) {
		ISQLGenerator gen = (ISQLGenerator) generators.get(operation.getDestName());
		if(gen != null) {
			return gen.generate(operation, runtime);
		}
		return null;
	}

}
