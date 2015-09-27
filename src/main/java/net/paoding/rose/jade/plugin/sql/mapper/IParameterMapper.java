/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.plugin.sql.Plum.Operator;
import net.paoding.rose.jade.plugin.sql.annotations.IgnoreNull;

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
	
	IgnoreNull getIgnoreNull();
	
	IOperationMapper getOperationMapper();
	
}
