/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.util.ArrayList;
import java.util.List;

import net.paoding.rose.jade.annotation.SQLParam;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ExpandableParameterMapper extends ParameterMapper implements IExpandableParameterMapper {
	
	private EntityMapperManager entityMapperManager;
	
	private List<IParameterMapper> expendedParameters;

	public ExpandableParameterMapper(IOperationMapper operationMapper, SQLParam original, Class<?> type) {
		super(operationMapper, original, type, null);
	}

	@Override
	public void doMap() {
		super.doMap();
		try {
			mapExpendedParameterMapper();
		} catch (Exception e) {
			throw new MappingException(e.getMessage());
		}
	}
	
	protected void mapExpendedParameterMapper() throws Exception {
		IEntityMapper entityMapper = entityMapperManager.createGet((Class<?>) getType());
		if(entityMapper != null) {
			List<IColumnMapper> columns = entityMapper.getColumns();
			expendedParameters = new ArrayList<IParameterMapper>(columns.size());
			for(IColumnMapper col : columns) {
				expendedParameters.add(createExpendedParameterMapper(col));
			}
		}
	}

	protected ParameterMapper createExpendedParameterMapper(IColumnMapper col) {
		ParameterMapper expended = new ParameterMapper(this, col);
		expended.map();
		return expended;
	}

	@Override
	public void setEntityMapperManager(EntityMapperManager entityMapperManager) {
		this.entityMapperManager = entityMapperManager;
	}

	@Override
	public List<IParameterMapper> expand() {
		return expendedParameters;
	}

}
