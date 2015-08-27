/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.plugin.sql.Entity;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class EntityMapperManager extends AbstractCachedMapperManager<Class<? extends Entity>, IEntityMapper> {
	
	public IEntityMapper create(Class<? extends Entity> clazz) {
		IEntityMapper entityMapper = new EntityMapper(clazz);
		entityMapper.map();
		return entityMapper;
	}

}
