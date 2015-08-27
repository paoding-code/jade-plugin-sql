/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IMapperManager<S, T extends IMapper<?>> {

	/**
	 * 通过源直接创建映射对象
	 * @param source
	 * @return
	 */
	T create(S source);
	
	/**
	 * 通过源获取映射对象，当未缓存过指定的源，则返回null。
	 * @param source
	 * @return
	 */
	T get(S source);
	
	/**
	 * 当指定的源不能获取
	 * @param source
	 * @return
	 * @throws MappingException
	 */
	T createGet(S source) throws MappingException;
}
