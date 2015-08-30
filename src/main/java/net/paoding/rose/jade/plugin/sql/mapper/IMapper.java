/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IMapper<O> {

	public O getOriginal();
	
	void map();
	
	String getOriginalName();
	
	String getName();
	
}
