/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class ExpandableParameterMapper extends ParameterMapper implements IExpandableParameterMapper {
	
	public ExpandableParameterMapper(IOperationMapper operationMapper,
			Class<?> type, Annotation[] annotations) {
		super(operationMapper, type, annotations);
	}

	private List<IParameterMapper> expendedParameters;

	@Override
	public void init() {
		super.init();
		try {
			mapExpendedParameterMapper();
		} catch (Exception e) {
			throw new MappingException(e.getMessage());
		}
	}
	
	protected void mapExpendedParameterMapper() throws Exception {
		IEntityMapper entityMapper = getOperationMapper().getTargetEntityMapper();
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
		expended.init();
		return expended;
	}

	@Override
	public List<IParameterMapper> expand() {
		return expendedParameters;
	}

}
