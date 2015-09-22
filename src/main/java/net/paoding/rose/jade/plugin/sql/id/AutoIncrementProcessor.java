/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

import net.paoding.rose.jade.statement.AfterInvocationCallback;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author cunxin.gz
 *
 */
public class AutoIncrementProcessor implements AfterInvocationCallback {

	/* (non-Javadoc)
	 * @see net.paoding.rose.jade.statement.AfterInvocationCallback#execute(net.paoding.rose.jade.statement.StatementRuntime, java.lang.Object)
	 */
	@Override
	public Object execute(StatementRuntime runtime, Object returnValue) {
		return returnValue;
	}

}
