/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;

import net.paoding.rose.jade.plugin.sql.Order;
import net.paoding.rose.jade.plugin.sql.annotations.Limit;
import net.paoding.rose.jade.plugin.sql.annotations.Offset;
import net.paoding.rose.jade.plugin.sql.annotations.Where;
import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ConditionalOperationMapper extends OperationMapper {
	
	private Integer orderParameterIndex = -1;
	
	private Integer offsetParameterIndex = -1;
	
	private Integer limitParameterIndex = -1;
	
	private Integer whereAt = -1;
	
	private long mode = 0;
	
	private static final long CONDITION_MODE_PRIMARY_KEY = 1;
	private static final long CONDITION_MODE_COMPLEX = 1 << 1;
	private static final long CONDITION_MODE_ENTITY = 1 << 2;
	private static final long CONDITION_MODE_ENTITY_COLLECTION = 1 << 3;
	
	public ConditionalOperationMapper(StatementMetaData original) {
		super(original);
	}
	
	@Override
	protected void mapParameters() {
		if(original.getMethod().getParameterCount() == 1) {
			Class<?> paramType = original.getMethod().getParameterTypes()[0];
			if(getPrimaryKeyType().isAssignableFrom(paramType)) {
				// 仅有一个参数，并且类型为泛型规定的主键类型，则视为主键条件模式。
				appendMode(CONDITION_MODE_PRIMARY_KEY);
				return;
			}
		}
		super.mapParameters();
		appendMode(CONDITION_MODE_COMPLEX);
	}
	
	private void appendMode(long append) {
		mode = mode | append;
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
					// 偏移量
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
					// 最大返回记录数
					if(containsLimit()) {
						throw new MappingException("Multiple limit parameter.");
					}
					if(!Number.class.isAssignableFrom(type)
							&& type != int.class
							&& type != long.class) {
						throw new MappingException("The offset parameter must be int, long or Number.");
					}
					limitParameterIndex = index;
				} else if(annotation.annotationType() == Where.class) {
					// where条件的位置，用于update。
					if(index == 0) {
						throw new MappingException("The annotation \"Where\" can not appear at the first parameter.");
					}
					whereAt = index;
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
	
	public boolean containsWhere() {
		return whereAt != -1;
	}

	public Integer getWhereAt() {
		return whereAt;
	}
	
	public boolean isPrimaryKeyConditionMode() {
		return mode == CONDITION_MODE_PRIMARY_KEY;
	}
	
	public boolean isComplexConditionMode() {
		return mode == CONDITION_MODE_COMPLEX;
	}
	
}
