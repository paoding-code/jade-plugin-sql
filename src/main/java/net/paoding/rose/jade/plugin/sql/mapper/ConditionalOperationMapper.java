/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;

import net.paoding.rose.jade.plugin.sql.Order;
import net.paoding.rose.jade.plugin.sql.annotations.Limit;
import net.paoding.rose.jade.plugin.sql.annotations.Offset;
import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ConditionalOperationMapper extends OperationMapper {
	
	private Integer orderParameterIndex = -1;
	
	private Integer offsetParameterIndex = -1;
	
	private Integer limitParameterIndex = -1;
	
	public ConditionalOperationMapper(StatementMetaData original) {
		super(original);
	}
	
	@Override
	protected IParameterMapper createParameterMapper(Class<?> type, Annotation[] annotations, int index) {
		if(Order.class.isAssignableFrom(type)) {
			orderParameterIndex = index;
			return null;
		} else {
			for(int i = 0; i < annotations.length; i++) {
				Annotation annotation = annotations[i];
				if(annotation.annotationType() == Offset.class) {
					if(containsOffset()) {
						throw new MappingException("Multiple offset parameter.");
					}
					if(!Number.class.isAssignableFrom(type)
							&& type != int.class
							&& type != long.class) {
						throw new MappingException("The offset parameter must be int, long or Number.");
					}
					offsetParameterIndex = index;
				} else if(annotation.annotationType() == Limit.class) {
					if(containsLimit()) {
						throw new MappingException("Multiple limit parameter.");
					}
					if(!Number.class.isAssignableFrom(type)
							&& type != int.class
							&& type != long.class) {
						throw new MappingException("The offset parameter must be int, long or Number.");
					}
					limitParameterIndex = index;
				}
			}
		}
		return super.createParameterMapper(type, annotations, index);
	}
	
	@Override
	protected void doMap() {
		super.doMap();
	}
	
	public boolean containsOrder() {
		return orderParameterIndex != -1 || getTargetEntityMapper().getDefaultOrder() != null;
	}
	
	public Integer getOrderParameterIndex() {
		return orderParameterIndex;
	}
	
	public boolean containsOffset() {
		return offsetParameterIndex != -1;
	}
	
	public boolean containsLimit() {
		return limitParameterIndex != -1;
	}

	public Integer getOffsetParameterIndex() {
		return offsetParameterIndex;
	}

	public Integer getLimitParameterIndex() {
		return limitParameterIndex;
	}
	
}
