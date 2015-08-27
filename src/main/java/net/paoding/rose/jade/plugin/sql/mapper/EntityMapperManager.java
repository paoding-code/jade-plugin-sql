/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class EntityMapperManager extends AbstractCachedMapperManager<Class<?>, IEntityMapper> {
	
	public IEntityMapper create(Class<?> clazz) {
		IEntityMapper entityMapper = new EntityMapper(clazz);
		entityMapper.map();
		return entityMapper;
	}

}
