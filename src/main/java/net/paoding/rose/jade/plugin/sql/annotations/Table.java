/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识JavaBean对数据库标的映射关系
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	/**
	 * 表名，不指定则按规范自动生成。
	 * @return
	 */
	String value() default "";
}
