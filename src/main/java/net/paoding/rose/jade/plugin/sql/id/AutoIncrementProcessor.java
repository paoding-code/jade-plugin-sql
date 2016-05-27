/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.id;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.paoding.rose.jade.plugin.sql.PlumSQLInterpreter;
import net.paoding.rose.jade.plugin.sql.PlumSQLInterpreter.SQLGeneratorInterpreter;
import net.paoding.rose.jade.plugin.sql.mapper.IColumnMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IEntityMapper;
import net.paoding.rose.jade.plugin.sql.mapper.IOperationMapper;
import net.paoding.rose.jade.statement.AfterInvocationCallback;
import net.paoding.rose.jade.statement.DynamicReturnGeneratedKeys;
import net.paoding.rose.jade.statement.StatementMetaData;
import net.paoding.rose.jade.statement.StatementRuntime;

/**
 * @author cunxin.gz
 *
 */
public class AutoIncrementProcessor extends DynamicReturnGeneratedKeys implements AfterInvocationCallback {
	
	private Log logger = LogFactory.getLog(getClass());

	/* (non-Javadoc)
	 * @see net.paoding.rose.jade.statement.AfterInvocationCallback#execute(net.paoding.rose.jade.statement.StatementRuntime, java.lang.Object)
	 */
	@Override
	public Object execute(StatementRuntime runtime, Object returnValue) {
		SQLGeneratorInterpreter attribute = runtime.getMetaData().getAttribute(PlumSQLInterpreter.ATTRIBUTE_NAME_INTERPRETER);
		if(attribute != null) {
			IOperationMapper operationMapper = attribute.getOperationMapper();
			
			if(IOperationMapper.OPERATION_INSERT.equals(operationMapper.getDestName())) {
				IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
				
				if(targetEntityMapper.containsAutoIncrementPrimaryKey()) {
					if(returnValue.getClass().isArray()) {
						
					} else {
						Object param = runtime.getParameters().get(":1");
						List<IColumnMapper> primaryKey = targetEntityMapper.getPrimaryKey();
						IColumnMapper iColumnMapper = primaryKey.get(0);
						try {
							iColumnMapper.getOriginal().set(param, returnValue);
						} catch (Exception e) {
							logger.error("Error to set auto increment value to entity object.", e);
						}
					}
				}
			}
			
		}
		return returnValue;
	}

	@Override
	public boolean shouldReturnGerneratedKeys(StatementRuntime runtime) {
		return isAutoIncrementPrimaryKey(runtime.getMetaData());
	}
	
	@Override
	public void checkMethodReturnType(Class<?> returnType, StatementMetaData metaData) {
		if(isAutoIncrementPrimaryKey(metaData)) {
			super.checkMethodReturnType(returnType, metaData);
		}
	}
	
	private boolean isAutoIncrementPrimaryKey(StatementMetaData metaData) {
		SQLGeneratorInterpreter attribute = metaData.getAttribute(PlumSQLInterpreter.ATTRIBUTE_NAME_INTERPRETER);
		
		if(attribute != null) {
			IOperationMapper operationMapper = attribute.getOperationMapper();
			
			if(IOperationMapper.OPERATION_INSERT.equals(operationMapper.getDestName())) {
				IEntityMapper targetEntityMapper = operationMapper.getTargetEntityMapper();
				return targetEntityMapper.containsAutoIncrementPrimaryKey();
			}
		}
		return false;
	}

}
