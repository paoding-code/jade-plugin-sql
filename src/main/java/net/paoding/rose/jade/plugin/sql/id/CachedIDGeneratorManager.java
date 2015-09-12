/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author Alan
 *
 */
public class CachedIDGeneratorManager implements IDGeneratorManager {
	
	private final CacheLoader<Class<? extends IDGenerator<?>>, IDGenerator<?>> loader = new CacheLoader<Class<? extends IDGenerator<?>>, IDGenerator<?>>() {

		@Override
		public IDGenerator<?> load(Class<? extends IDGenerator<?>> key)
				throws Exception {
			return create(key);
		}
	};
	
	private LoadingCache<Class<? extends IDGenerator<?>>, IDGenerator<?>> cache = CacheBuilder.newBuilder().build(loader);

	/* (non-Javadoc)
	 * @see net.paoding.rose.jade.plugin.sql.id.IDGeneratorManager#get(java.lang.Class)
	 */
	@Override
	public IDGenerator<?> get(Class<? extends IDGenerator<?>> type) {
		try {
			return cache.get(type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public IDGenerator<?> create(Class<? extends IDGenerator<?>> type) {
		try {
			return type.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
