/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当参数列表中任意一项被标记，那么其后的所有参数将被应用到WHERE子句。<br>
 * 仅在更新操作中生效，并且不能标记在第一个参数前。
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Where {

}
