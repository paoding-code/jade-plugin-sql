/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.annotations;

/**
 * 当参数列表中任意一项被标记，那么其后的所有参数将被应用到WHERE子句。<br>
 * 仅在Update方法中生效，并且不能标记第一个参数。
 * @author Alan.Geng[gengzhi718@gmail.com]
 */
public @interface Where {

}
