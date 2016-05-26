/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Field;

import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.annotations.Column;
import net.paoding.rose.jade.plugin.sql.id.IDGenerator;
import net.paoding.rose.jade.plugin.sql.id.NonID;
import net.paoding.rose.jade.plugin.sql.util.PlumUtils;

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
	protected String generateOriginalName() {
		return original.getName();
	}
	
	@Override
	public void init() {
		defaultOrderDirection = annotation.order();
		super.init();
	}
	
	@Override
	public String generateName(String source) {
		if(PlumUtils.isNotBlank(annotation.value())) {
			return annotation.value();
		}
		return super.generateName(source);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isPrimaryKey() {
		return annotation.id() != NonID.class || annotation.pk();
	}
	
	public Direction getDefaultOrderDirection() {
		return defaultOrderDirection;
	}
	
	@Override
	public Class<? extends IDGenerator<?>> getIDGeneratorType() {
		return annotation != null ? annotation.id() : null;
	}

}
