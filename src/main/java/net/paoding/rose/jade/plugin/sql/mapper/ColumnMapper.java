/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Field;

import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.annotations.Column;
import net.paoding.rose.jade.plugin.sql.util.MyLangUtils;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ColumnMapper extends AbstractMapper<Field> implements IColumnMapper {
	
	private Column annotation;
	
	private Direction defaultOrderDirection;

	public ColumnMapper(Field from) {
		super(from);
		annotation = original.getAnnotation(Column.class);
		from.setAccessible(true);
	}

	@Override
	protected String getNameSource() {
		return original.getName();
	}
	
	@Override
	protected void doMap() {
		defaultOrderDirection = annotation.order();
		super.doMap();
	}
	
	@Override
	public String generateName(String source) {
		if(MyLangUtils.isNotBlank(annotation.value())) {
			return annotation.value();
		}
		return super.generateName(source);
	}

	@Override
	public boolean isPrimaryKey() {
		return annotation.pk();
	}
	
	public Direction getDefaultOrderDirection() {
		return defaultOrderDirection;
	}

}
