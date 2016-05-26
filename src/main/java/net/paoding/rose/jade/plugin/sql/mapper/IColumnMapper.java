/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Field;

import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.id.IDGenerator;

/**
 * 负责列的映射。
 * 
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IColumnMapper extends IMapper<Field> {

    /**
     * 该列是否是表的主键？
     * 
     * @return
     */
	boolean isPrimaryKey();
	
	/**
	 * 该列的默认排序规则，可用在SQL查询语句
	 * 
	 * @return
	 */
	Direction getDefaultOrderDirection();
	
	/**
	 * 主键生成器
	 * 
	 * @return
	 */
	Class<? extends IDGenerator<?>> getIDGeneratorType();
}
