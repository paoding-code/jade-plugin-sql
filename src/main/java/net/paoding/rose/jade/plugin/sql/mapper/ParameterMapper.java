/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.plugin.sql.Plum.Operator;
import net.paoding.rose.jade.plugin.sql.annotations.Eq;
import net.paoding.rose.jade.plugin.sql.annotations.Ge;
import net.paoding.rose.jade.plugin.sql.annotations.Gt;
import net.paoding.rose.jade.plugin.sql.annotations.Le;
import net.paoding.rose.jade.plugin.sql.annotations.Like;
import net.paoding.rose.jade.plugin.sql.annotations.Limit;
import net.paoding.rose.jade.plugin.sql.annotations.Lt;
import net.paoding.rose.jade.plugin.sql.annotations.Offset;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ParameterMapper extends AbstractMapper<SQLParam> implements IParameterMapper {
	
	private IParameterMapper parent;
	
	private IColumnMapper columnMapper;
	
	private String originalName;
	
	private Class<?> type;
	
	private Operator operator = Operator.EQ;
	
	private Annotation[] annotations;
	
	private static final Map<Class<? extends Annotation>, Operator> OPERATORS;
	
	static {
		Map<Class<? extends Annotation>, Operator> operators = new HashMap<Class<? extends Annotation>, Operator>(Operator.values().length);
		
		operators.put(Like.class, Operator.LIKE);
		
		operators.put(Offset.class, Operator.OFFSET);
		operators.put(Limit.class, Operator.LIMIT);
		
		operators.put(Eq.class, Operator.EQ);
		operators.put(Ge.class, Operator.GE);
		operators.put(Gt.class, Operator.GT);
		operators.put(Le.class, Operator.LE);
		operators.put(Lt.class, Operator.LT);
		
		OPERATORS = Collections.unmodifiableMap(operators);
	}

	public ParameterMapper(IParameterMapper parent, IColumnMapper columnMapper) {
		super(parent.getOriginal());
		this.parent = parent;
		this.columnMapper = columnMapper;
		originalName = parent.getOriginal().value() + "." + columnMapper.getOriginal().getName();
	}
	
	public ParameterMapper(SQLParam original, Class<?> type, Annotation[] annotations) {
		super(original);
		this.type = type;
		originalName = original.value();
		this.annotations = annotations;
	}
	
	@Override
	protected void doMap() {
		super.doMap();
		if(annotations != null && annotations.length > 0) {
			for(Annotation annotation : annotations) {
				Operator operator = OPERATORS.get(annotation.annotationType());
				if(operator != null) {
					this.operator = operator;
					break;
				}
			}
		}
	}

	@Override
	public Class<?> getType() {
		if(parent == null) {
			return type;
		} else {
			return columnMapper.getOriginal().getType();
		}
	}

	@Override
	protected String getNameSource() {
		if(parent == null) {
			return original.value();
		} else {
			return null;
		}
	}
	
	@Override
	public String getName() {
		if(parent == null) {
			return super.getName();
		} else {
			return columnMapper.getName();
		}
	}
	
	public String getOriginalName() {
		return originalName;
	}
	
	public IParameterMapper getParent() {
		return parent;
	}

	public IColumnMapper getColumnMapper() {
		return columnMapper;
	}

	public Operator getOperator() {
		return operator;
	}

}
