/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

/**
 * @author Alan
 *
 */
public interface IDGenerator<T> {

	public T generate(Object entity);
	
}
