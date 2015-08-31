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
	
	private static final ThreadLocal<Context> LOCAL = new ThreadLocal<Context>();
	
	public static enum Operator {
		
		/**
		 * 等于
		 */
		EQ,
		
		/**
		 * 不等于
		 */
		NE,
		
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
		 * in
		 */
		IN,
		
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
	
	private static Context getContext() {
		Context context = LOCAL.get();
		if(context == null) {
			context = new Context();
			LOCAL.set(context);
		}
		return context;
	}
	
	/**
	 * Null值是否参与到查询条件、更新操作等。
	 * @return
	 */
	public static boolean isAffectedNull() {
		Context context = LOCAL.get();
		return context != null && context.isAffectedNull();
	}
	
	/**
	 * 执行Null值生效的操作
	 * @param operation
	 * @return
	 */
	public static <T> T affectedNull(Operation<T> operation) {
		Context context = getContext();
		try {
			context.setAffectedNull(true);
			return operation.exec();
		} finally {
			context.setAffectedNull(false);
		}
	}
	
	public static class Context {
		
		private boolean affectedNull = false;

		public boolean isAffectedNull() {
			return affectedNull;
		}

		public void setAffectedNull(boolean affectedNull) {
			this.affectedNull = affectedNull;
		}

	}
	
}
