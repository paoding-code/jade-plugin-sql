/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.annotation.SQLType;
import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class OperationMapperManager extends AbstractCachedMapperManager<StatementMetaData, IOperationMapper> {
	
	private EntityMapperManager entityMapperManager;

	@Override
	public IOperationMapper create(StatementMetaData source) {
		IOperationMapper mapper = null;
		
		if(source.getSQLType() == SQLType.READ) {
			mapper = new ConditionalOperationMapper(source);
		} else {
			mapper = new OperationMapper(source);
		}
		
		mapper.setEntityMapperManager(entityMapperManager);
		mapper.map();
		return mapper;
	}

	public void setEntityMapperManager(EntityMapperManager entityMapperManager) {
		this.entityMapperManager = entityMapperManager;
	}

}
