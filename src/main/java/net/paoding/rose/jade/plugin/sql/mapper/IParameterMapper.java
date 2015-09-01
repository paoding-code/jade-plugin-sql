/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.plugin.sql.Plum.Operator;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IParameterMapper extends IMapper<ParameterOriginal> {

	String getOriginalName();
	
	Class<?> getType();
	
	IColumnMapper getColumnMapper();
	
	Operator getOperator();
	
	IParameterMapper getParent();
	
	boolean isIgnoreNull();
}
