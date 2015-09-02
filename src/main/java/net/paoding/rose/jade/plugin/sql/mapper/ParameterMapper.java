/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.plugin.sql.Plum;
import net.paoding.rose.jade.plugin.sql.Plum.Operator;
import net.paoding.rose.jade.plugin.sql.annotations.Eq;
import net.paoding.rose.jade.plugin.sql.annotations.Ge;
import net.paoding.rose.jade.plugin.sql.annotations.Gt;
import net.paoding.rose.jade.plugin.sql.annotations.IgnoreNull;
import net.paoding.rose.jade.plugin.sql.annotations.In;
import net.paoding.rose.jade.plugin.sql.annotations.Le;
import net.paoding.rose.jade.plugin.sql.annotations.Like;
import net.paoding.rose.jade.plugin.sql.annotations.Limit;
import net.paoding.rose.jade.plugin.sql.annotations.Lt;
import net.paoding.rose.jade.plugin.sql.annotations.Ne;
import net.paoding.rose.jade.plugin.sql.annotations.Offset;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ParameterMapper extends AbstractMapper<ParameterOriginal> implements IParameterMapper {
	
	private IOperationMapper operationMapper;
	
	private IParameterMapper parent;
	
	private IColumnMapper columnMapper;
	
	private IgnoreNull ignoreNull;
	
	private Operator operator = Operator.EQ;
	
	private static final Map<Class<? extends Annotation>, Operator> OPERATORS;
	
	static {
		Map<Class<? extends Annotation>, Operator> operators = new HashMap<Class<? extends Annotation>, Operator>(Operator.values().length);
		
		operators.put(Like.class, Operator.LIKE);
		operators.put(In.class, Operator.IN);
		
		operators.put(Offset.class, Operator.OFFSET);
		operators.put(Limit.class, Operator.LIMIT);
		
		operators.put(Eq.class, Operator.EQ);
		operators.put(Ne.class, Operator.NE);
		operators.put(Ge.class, Operator.GE);
		operators.put(Gt.class, Operator.GT);
		operators.put(Le.class, Operator.LE);
		operators.put(Lt.class, Operator.LT);
		
		OPERATORS = Collections.unmodifiableMap(operators);
	}

	public ParameterMapper(IParameterMapper parent, IColumnMapper columnMapper) {
		super(new ParameterOriginal(null, columnMapper.getOriginal().getType(), null));
		this.parent = parent;
		this.columnMapper = columnMapper;
	}
	
	public ParameterMapper(IOperationMapper operationMapper, SQLParam original, Class<?> type, Annotation[] annotations) {
		super(new ParameterOriginal(original, type, annotations));
		this.operationMapper = operationMapper;
	}
	
	@Override
	protected void doMap() {
		super.doMap();
		Annotation[] annotations = original.getAnnotations();
		if(annotations != null && annotations.length > 0) {
			for(Annotation annotation : annotations) {
				if(annotation.annotationType() == IgnoreNull.class) {
					ignoreNull = (IgnoreNull) annotation;
				} else {
					Operator operator = OPERATORS.get(annotation.annotationType());
					if(operator != null) {
						this.operator = operator;
					}
				}
			}
		}
	}

	@Override
	public Class<?> getType() {
		return original.getType();
	}

	@Override
	protected String generateOriginalName() {
		return original.getName();
	}
	
	@Override
	public IParameterMapper getParent() {
		return parent;
	}

	public IColumnMapper getColumnMapper() {
		return columnMapper;
	}

	public Operator getOperator() {
		return operator;
	}

	public boolean isIgnoreNull() {
		IgnoreNull ignoreNull = getIgnoreNull();
		if(ignoreNull == null) {
			return Plum.DEFAULT_IGNORE_NULL;
		}
		
		return ignoreNull.value();
	}

	public IgnoreNull getIgnoreNull() {
		if(ignoreNull != null) {
			return ignoreNull;
		}
		
		IParameterMapper parent = getParent();
		if(parent != null) {
			return parent.getIgnoreNull();
		}
		
		if(operationMapper != null) {
			return operationMapper.getIgnoreNull();
		}
		return null;
	}
	
}
