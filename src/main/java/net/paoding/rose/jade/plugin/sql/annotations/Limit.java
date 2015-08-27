/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 最大返回记录数
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

}
