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
		operators.put(Operator.GE, " >= ");
		operators.put(Operator.GT, " > ");
		operators.put(Operator.LE, " <= ");
		operators.put(Operator.LT, " < ");
		operators.put(Operator.LIKE, " LIKE ");
		
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
		if(operationMapper.isPrimaryKeyConditionMode()) {
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
		} else if(operationMapper.isComplexConditionMode()) {
			List<IParameterMapper> parameters = operationMapper.getParameters();
			if(MyLangUtils.isNotEmpty(parameters)) {
				sql.append(" WHERE ");
				
				for(int i = 0; i < parameters.size(); i++) {
					IParameterMapper param = parameters.get(i);
					if(i > 0) {
						sql.append(" AND ");
					}
					applyCondition(param, runtime, i, sql);
				}
			}
		} else {
			throw new UnsupportedOperationException("Unknown condition mode.");
		}
	}
	
	protected void applyCondition(IParameterMapper param, StatementRuntime runtime, int index, StringBuilder sql) {
		sql.append(param.getName());
		
		Operator op = param.getOperator();
		sql.append(OPERATORS.get(op));
		sql.append(":");
		
		if(op != Operator.LIKE && op != Operator.EQ) {
			// Multiple parameter value at the same column.(e.g. age >= 15 AND age < 22)
			// In "Jade" framework, Parameter value appears first will be overwritten when the same name in annotation "SQLParam".
			sql.append(index + 1);
		} else {
			// Normally, the "like" and "=" condition only once at the same column.
			sql.append(param.getOriginal().value());
		}
	}
	
	protected void beforeApplyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		
	}
	
	protected void afterApplyConditions(ConditionalOperationMapper operationMapper, StatementRuntime runtime, StringBuilder sql) {
		
	}

}
