/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.util.List;

import net.paoding.rose.jade.plugin.sql.Order;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IEntityMapper extends IMapper<Class<?>> {
	
	List<IColumnMapper> getColumns();
	
	List<IColumnMapper> getPrimaryKey();
	
	IColumnMapper getColumnMapperByFieldName(String fieldName);

	Order getDefaultOrder();
}
