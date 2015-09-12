/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Field;

import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.id.IDGenerator;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IColumnMapper extends IMapper<Field> {

	boolean isPrimaryKey();
	
	Direction getDefaultOrderDirection();
	
	Class<? extends IDGenerator<?>> getIDGeneratorType();
}
