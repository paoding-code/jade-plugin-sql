/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

import java.util.UUID;

/**
 * @author Alan
 *
 */
public class UUIDStringGenerator implements IDGenerator<String> {

	/* (non-Javadoc)
	 * @see net.paoding.rose.jade.plugin.sql.util.IDGenerator#generate(java.lang.Object)
	 */
	@Override
	public String generate(Object entity) {
		return UUID.randomUUID().toString();
	}

}
