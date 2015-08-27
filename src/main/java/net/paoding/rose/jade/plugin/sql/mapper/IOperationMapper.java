/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.util.List;

import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface IOperationMapper extends IMapper<StatementMetaData> {
	
	public static final String OPERATION_SELECT = "SELECT";
	public static final String OPERATION_INSERT = "INSERT";
	public static final String OPERATION_DELETE = "DELETE";
	public static final String OPERATION_UPDATE = "UPDATE";
	
	IEntityMapper getTargetEntityMapper();
	
	List<IParameterMapper> getParameters();
	
	boolean containsParameter();
	
	void setEntityMapperManager(EntityMapperManager entityMapperManager);
	
}
