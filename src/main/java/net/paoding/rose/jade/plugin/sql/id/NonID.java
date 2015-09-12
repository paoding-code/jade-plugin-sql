/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

/**
 * @author Alan
 *
 */
public final class NonID implements IDGenerator<Void> {

	/* (non-Javadoc)
	 * @see net.paoding.rose.jade.plugin.sql.util.IDGenerator#generate(java.lang.Object)
	 */
	@Override
	public Void generate(Object entity) {
		return null;
	}

}
