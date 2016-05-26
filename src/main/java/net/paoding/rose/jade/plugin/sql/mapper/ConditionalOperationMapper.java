/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;

import java.util.Collection;

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
	
	private static final long MODE_PRIMARY_KEY = 1;
	private static final long MODE_COMPLEX = 1 << 1;
	private static final long MODE_ENTITY = 1 << 2;
	private static final long MODE_ENTITY_COLLECTION = 1 << 3;
	
	public ConditionalOperationMapper(StatementMetaData original) {
		super(original);
	}
	
	private void appendMode(long append) {
		mode = mode | append;
	}
	
	@Override
	protected IParameterMapper createParameterMapper(Class<?> type, Annotation[] annotations, int index) {
		if(Order.class.isAssignableFrom(type)) {
			// Order类型的参数仅记录其在参数列表中的位置，在查询操作的切面中，通过位置获取其实际的值。
			orderParameterIndex = index;
			return null;
		}
		
		if(annotations.length == 0
				&& getPrimaryKeyType().isAssignableFrom(type)
				&& original.getMethod().getParameterTypes().length == 1) {
			// 类型为泛型中的主键类型
			appendMode(MODE_PRIMARY_KEY);
		} else if(getEntityType().isAssignableFrom(type)
				&& original.getMethod().getParameterTypes().length == 1) {
			if(Collection.class.isAssignableFrom(original.getMethod().getParameterTypes()[index])) {
				// 实际类型为集合
				appendMode(MODE_ENTITY_COLLECTION);
			} else {
				// 类型为泛型中的实体类型
				appendMode(MODE_ENTITY);
			}
		}
		
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
			} else if(annotation.annotationType() == Where.class
					&& OPERATION_UPDATE.equals(getDestName())) {
				// Where条件的位置，用于更新操作，其他操作该注解无任何意义。
				if(index == 0) {
					// 如果该注解被标记在第一个参数前，证明该操作没有任何值用于更新。
					throw new MappingException("When update operation, the annotation \"Where\" cannot appear before at first parameter.");
				}
				whereAt = index;
			}
			appendMode(MODE_COMPLEX);
		}
		
		return super.createParameterMapper(type, annotations, index);
		
		// :) Have to say, [Casablanca - Bertie Higgins] very nice!
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
	
	public boolean isPrimaryKeyMode() {
		return isSpecifiedMode(MODE_PRIMARY_KEY);
	}
	
	public boolean isComplexMode() {
		return isSpecifiedMode(MODE_COMPLEX);
	}
	
	public boolean isEntityMode() {
		return isSpecifiedMode(MODE_ENTITY);
	}
	
	public boolean isEntityCollectionMode() {
		return isSpecifiedMode(MODE_ENTITY_COLLECTION);
	}
	
	private boolean isSpecifiedMode(long mode) {
		return (this.mode & mode) == mode;
	}
	
}
