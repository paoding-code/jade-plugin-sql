/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.util.List;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IExpandableParameterMapper extends IParameterMapper {

	List<IParameterMapper> expand();
	
}
