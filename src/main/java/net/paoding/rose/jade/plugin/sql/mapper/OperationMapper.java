/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.plugin.sql.Entity;
import net.paoding.rose.jade.plugin.sql.GenericDAO;
import net.paoding.rose.jade.statement.StatementMetaData;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class OperationMapper extends AbstractMapper<StatementMetaData> implements IOperationMapper {
	
	private IEntityMapper targetEntityMapper;
	
	private List<IParameterMapper> parameters;
	
	private EntityMapperManager entityMapperManager;
	
	private Class<?> entityType;
	
	public static final List<IParameterMapper> NO_PARAMETER = Collections.unmodifiableList(new ArrayList<IParameterMapper>());
	
	public static final String[] OPERATION_KEYS = {
		OPERATION_SELECT,
		OPERATION_INSERT,
		OPERATION_DELETE,
		OPERATION_UPDATE
	};
	
	public static final String OPERATION_PREFIX[][] = {
		{"get", "find"},
		{"save", "insert"},
		{"delete", "remove"},
		{"update"}
	};
	
	public OperationMapper(StatementMetaData original) {
		super(original);
	}

	@Override
	public IEntityMapper getTargetEntityMapper() {
		return targetEntityMapper;
	}

	@Override
	protected void doMap() {
		mapGenericEntityType();
		mapTargetEntityMapper();
		mapParameters();
	}
	
	protected void mapGenericEntityType() {
		entityType = original.getDAOMetaData().resolveTypeVariable(GenericDAO.class, "E");
		if(entityType == null) {
			throw new MappingException("Cannot find the generic type.");
		}
	}

	@SuppressWarnings("unchecked")
	protected void mapTargetEntityMapper() {
		targetEntityMapper = entityMapperManager.createGet((Class<? extends Entity>) entityType);
	}
	
	protected void mapParameters() {
		Method method = original.getMethod();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		
		if(parameterAnnotations == null
				|| parameterAnnotations.length == 0) {
			parameters = NO_PARAMETER;
			return;
		}
		
		if(getName() == OPERATION_INSERT
				&& parameters == NO_PARAMETER) {
			throw new MappingException("The insert operation must has least 1 parameters.");
		}
		
		parameters = new ArrayList<IParameterMapper>(parameterAnnotations.length);
		
		for(int i = 0; i < parameterAnnotations.length; i++) {
			// TODO: Check the parameter type.
			IParameterMapper parameterMapper = createParameterMapper(parameterTypes[i], parameterAnnotations[i], i);
			if(parameterMapper != null) {
				parameters.add(parameterMapper);
			}
		}
	}
	
	protected Type[] getCollectionTypeBounds(Class<?> collectionType) {
		TypeVariable<?>[] typeParameters = collectionType.getTypeParameters();
		if(typeParameters.length == 0) {
			throw new MappingException("The generic type of collection must be specified.");
		}
		TypeVariable<?> typeVariable = collectionType.getTypeParameters()[0];
		return typeVariable.getBounds();
	}
	
	@SuppressWarnings("unchecked")
	protected IParameterMapper createParameterMapper(Class<?> type, Annotation[] annotations, int index) {
		if(annotations != null && annotations.length > 0) {
			for(Annotation annotation : annotations) {
				if(annotation.annotationType().equals(SQLParam.class)) {
					if(type == Entity.class) {
						type = entityType;
					} else if(Collection.class.isAssignableFrom(type)) {
						type = (Class<?>) getCollectionTypeBounds(type)[0];
						if(type == Object.class) {
							type = entityType;
						}
					}
					
					IParameterMapper param = null;
					if(isExpandableParameterType(type)) {
						param = new ExpandableParameterMapper((SQLParam) annotation, (Class<? extends Entity>) type);
						((ExpandableParameterMapper) param).setEntityMapperManager(entityMapperManager);
					} else {
						param = new ParameterMapper((SQLParam) annotation, type, annotations);
					}
					
					param.map();
					return param;
				}
			}
		}
		return null;
	}
	
	protected boolean isExpandableParameterType(Class<?> type) {
		if(Entity.class.isAssignableFrom(type)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsParameter() {
		return parameters != NO_PARAMETER;
	}

	@Override
	protected String getNameSource() {
		return original.getMethod().getName();
	}
	
	@Override
	public String generateName(String source) {
		for(int i = 0; i < OPERATION_PREFIX.length; i++) {
			String[] prefixs = OPERATION_PREFIX[i];
			for(int j = 0; j < prefixs.length; j++) {
				if(source.startsWith(prefixs[j])) {
					return OPERATION_KEYS[i];
				}
			}
		}
		throw new MappingException("Unsupported method \"" + source + "\".");
	}

	public void setEntityMapperManager(EntityMapperManager entityMapperManager) {
		this.entityMapperManager = entityMapperManager;
	}

	@Override
	public List<IParameterMapper> getParameters() {
		return parameters;
	}
	
}
