/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Field;

import net.paoding.rose.jade.plugin.sql.Order.Direction;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IColumnMapper extends IMapper<Field> {

	boolean isPrimaryKey();
	
	Direction getDefaultOrderDirection();
}
