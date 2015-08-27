/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 缓存映射管理器
 * @author cunxin.gz
 */
public abstract class AbstractCachedMapperManager<S, T extends IMapper<?>> implements IMapperManager<S, T> {

	private LoadingCache<S, T> cache = CacheBuilder.newBuilder().build(new CacheLoader<S, T>(){

		@Override
		public T load(S key) throws Exception {
			return create(key);
		}
		
	});
	
	@Override
	public T get(S source) {
		return cache.getIfPresent(source);
	}
	
	@Override
	public T createGet(S source) throws MappingException {
		try {
			return cache.get(source);
		} catch (ExecutionException e) {
			if(e.getCause() instanceof MappingException) {
				throw (MappingException) e.getCause();
			} else {
				throw new MappingException("Unknown error.", e);
			}
		}
	}
}
