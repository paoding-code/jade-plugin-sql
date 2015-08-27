/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.plugin.sql.Plum.Operator;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IParameterMapper extends IMapper<SQLParam> {

	String getOriginalName();
	
	Class<?> getType();
	
	IColumnMapper getColumnMapper();
	
	Operator getOperator();
	
}
