/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询数据的偏移量
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Offset {

}
