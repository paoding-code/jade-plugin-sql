/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

/**
 * @author Alan
 *
 */
public interface IDGeneratorManager {
	
	IDGenerator<?> create(Class<? extends IDGenerator<?>> type);

	IDGenerator<?> get(Class<? extends IDGenerator<?>> type);
	
}
