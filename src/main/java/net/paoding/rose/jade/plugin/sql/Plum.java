/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import net.paoding.rose.jade.plugin.sql.Order.Direction;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class Plum {
	
	public static final String C = "INSERT @C";
	public static final String R = "SELECT @R";
	public static final String U = "UPDATE @U";
	public static final String D = "DELETE @D";
	
	public static enum Operator {
		
		/**
		 * 等于
		 */
		EQ,
		
		/**
		 * 大于
		 */
		GE,
		
		/**
		 * 小于
		 */
		LE,
		
		/**
		 * 大于等于
		 */
		GT,
		
		/**
		 * 小于等于
		 */
		LT,
		
		/**
		 * like
		 */
		LIKE,
		
		/**
		 * 查询区间的起始
		 */
		OFFSET,
		
		/**
		 * 限制查询条目数
		 */
		LIMIT;
	}
	
	/**
	 * 升序
	 * @param fields
	 * @return
	 */
	public static Order asc(String ... fields) {
		return new Order(fields, Direction.ASC);
	}
	
	/**
	 * 降序
	 * @param fields
	 * @return
	 */
	public static Order desc(String ... fields) {
		return new Order(fields, Direction.DESC);
	}
	
	/**
	 * 按指定方式排序
	 * @param direction
	 * @param fields
	 * @return
	 */
	public static Order orderBy(Direction direction, String... fields) {
		return new Order(fields, direction);
	}
	
}
