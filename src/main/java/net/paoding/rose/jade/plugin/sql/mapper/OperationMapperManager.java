/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Method;

import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class OperationMapperManager extends AbstractCachedMapperManager<StatementMetaData, IOperationMapper> {
	
	private EntityMapperManager entityMapperManager;
	
	private static final String[] INSERT_PREFIX = IOperationMapper.OPERATION_PREFIX[1];

	@Override
	public IOperationMapper create(StatementMetaData source) {
		IOperationMapper mapper = null;
		
		if(isInsert(source.getMethod())) {
			mapper = new OperationMapper(source);
		} else {
			mapper = new ConditionalOperationMapper(source);
		}
		
		mapper.setEntityMapperManager(entityMapperManager);
		mapper.init();
		return mapper;
	}
	
	private boolean isInsert(Method method) {
		for(String prefix : INSERT_PREFIX) {
			if(method.getName().startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public void setEntityMapperManager(EntityMapperManager entityMapperManager) {
		this.entityMapperManager = entityMapperManager;
	}

}
