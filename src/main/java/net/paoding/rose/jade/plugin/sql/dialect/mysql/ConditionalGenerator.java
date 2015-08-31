/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dialect.mysql;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.plugin.sql.Plum.Operator;
import net.paoding.rose.jade.plugin.sql.dialect.ISQLGenerator;
import net.paoding.rose.jade.plugin.sql.mapper.ConditionalOperationMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IColumnMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IParameterMapper;
import net.paoding.rose.jade.plugin.sql.util.MyLangUtils;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public abstract class ConditionalGenerator implements ISQLGenerator<ConditionalOperationMapper> {

	private static final Map<Operator, String> OPERATORS;
	
	static {
		Map<Operator, String> operators = new HashMap<Operator, String>(Operator.values().length);
		operators.put(Operator.EQ, " = ");
		operators.put(Operator.NE, " != ");
		operators.put(Operator.GE, " >= ");
		operators.put(Operator.GT, " > ");
		operators.put(Operator.LE, " <= ");
		operators.put(Operator.LT, " < ");
		operators.put(Operator.LIKE, " LIKE ");
		operators.put(Operator.IN, " IN ");
		
		OPERATORS = Collections.unmodifiableMap(operators);
	}
	
	@Override
	public String generate(ConditionalOperationMapper operationMapper, StatementRuntime runtime) {
		StringBuilder sql = new StringBuilder();
		beforeApplyConditions(operationMapper, runtime, sql);
		applyConditions(operationMapper, runtime, sql);
		afterApplyConditions(operationMapper, runtime, sql);
		return sql.toString();
	}
	
	protected void applyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		if(operationMapper.isPrimaryKeyMode()) {
			sql.append(" WHERE ");
			List<IColumnMapper> primaryKey = operationMapper.getTargetEntityMapper().getPrimaryKey();
			
			for(int i = 0; i < primaryKey.size(); i++) {
				IColumnMapper col = primaryKey.get(i);
				if(i > 0) {
					sql.append(" AND ");
				}
				
				sql.append(col.getName());
				sql.append(" = ");
				sql.append(":");
				
				// TODO:当实体为符合主键并且参数列表中的顺序与实体中字段顺序不一致，则会发生错误。
				sql.append(i + 1);
			}
		} else if(operationMapper.isComplexMode()) {
			List<IParameterMapper> parameters = operationMapper.getParameters();
			if(MyLangUtils.isNotEmpty(parameters)) {
				sql.append(" WHERE ");
				
				int i = operationMapper.getWhereAt();
				if(i < 0) {
					// 无任何参数被标记为Where，则视为所有参数都是Where条件。
					i = 0;
				}
				
				String and = "";
				for(; i < parameters.size(); i++) {
					IParameterMapper param = parameters.get(i);
					String condition = generateCondition(param, runtime, i);
					
					if(condition != null) {
						sql.append(and);
						sql.append(condition);
						and = " AND ";
					}
				}
			}
		} else {
			throw new UnsupportedOperationException("Unknown condition mode.");
		}
	}
	
	protected String generateCondition(IParameterMapper param, StatementRuntime runtime, int index) {
		Operator op = param.getOperator();
		
		if(!OPERATORS.containsKey(op)) {
			return null;
		}
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(param.getName());
		sql.append(OPERATORS.get(op));
		
		if(op == Operator.IN) {
			sql.append("(");
		}
		
		sql.append(":");
		
		
		if(op != Operator.LIKE
				&& op != Operator.EQ
				&& op != Operator.IN) {
			// Multiple parameter value at the same column.(e.g. age >= 15 AND age < 22)
			// In "Jade" framework, Parameter value appears first will be overwritten when the same name in annotation "SQLParam".
			sql.append(index + 1);
		} else {
			// Normally, the "like", "in" or "=" condition only once at the same column.
			sql.append(param.getOriginalName());
		}
		
		if(op == Operator.IN) {
			sql.append(")");
		}
		
		return sql.toString();
	}
	
	protected void beforeApplyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		
	}
	
	protected void afterApplyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		
	}

}
