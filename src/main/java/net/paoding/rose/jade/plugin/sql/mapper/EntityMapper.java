/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.plugin.sql.Order;
import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.Plum;
import net.paoding.rose.jade.plugin.sql.annotations.Column;
import net.paoding.rose.jade.plugin.sql.annotations.Table;
import net.paoding.rose.jade.plugin.sql.util.PlumUtils;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class EntityMapper extends AbstractMapper<Class<?>> implements IEntityMapper {
	
	private List<IColumnMapper> columns;
	
	private List<IColumnMapper> primaryKey;
	
	private Map<String, IColumnMapper> columnsMap;
	
	private static final String DATA_OBJECT_EXTENSION = "DO";
	
	private Order defaultOrder;
	
	public EntityMapper(Class<?> from) {
		super(from);
	}
	
	@Override
	protected void doMap() {
		super.doMap();
		this.columns = generateColumns();
		this.columnsMap = new HashMap<String, IColumnMapper>(this.columns.size());
		this.primaryKey = new ArrayList<IColumnMapper>(1);
		
		for(IColumnMapper col : columns) {
			if(this.columnsMap.containsKey(col.getOriginal().getName())) {
				logger.info("More than one name is " + col.getOriginal().getName() + " field was found, the nearest will be received.");
			}
			
			if(col.isPrimaryKey()) {
				primaryKey.add(col);
			}
			if(col.getDefaultOrderDirection() != Direction.NONE) {
				if(defaultOrder == null) {
					defaultOrder = Plum.orderBy(col.getDefaultOrderDirection(), col.getOriginal().getName());
				} else {
					defaultOrder.orderBy(col.getDefaultOrderDirection(), col.getName());
				}
			}
			this.columnsMap.put(col.getOriginal().getName(), col);
		}
	}
	
	protected List<IColumnMapper> generateColumns() {
		Class<?> entityClass = original;
		List<IColumnMapper> columns = new ArrayList<IColumnMapper>();
		
		while(entityClass != Object.class) {
			Field[] entityFields = entityClass.getDeclaredFields();
			for(Field entityField : entityFields) {
				if(entityField.isAnnotationPresent(Column.class)) {
					columns.add(createColumnMapper(entityField));
				}
			}
			entityClass = entityClass.getSuperclass();
		}
		
		if(columns.size() == 0) {
			throw new MappingException("No column field.");
		}
		
		return columns;
	}
	
	protected IColumnMapper createColumnMapper(Field field) {
		IColumnMapper cm = new ColumnMapper(field);
		cm.map();
		return cm;
	}
	
	@Override
	public List<IColumnMapper> getColumns() {
		return columns;
	}

	@Override
	protected String generateOriginalName() {
		String name = original.getSimpleName();
		if(name.endsWith(DATA_OBJECT_EXTENSION)) {
			name = name.substring(0, name.length() - 2);
		}
		return name;
	}
	
	@Override
	public String generateName(String source) {
		Table table = original.getAnnotation(Table.class);
		if(table != null && PlumUtils.isNotBlank(table.value())) {
			return table.value();
		}
		return super.generateName(source);
	}

	@Override
	public List<IColumnMapper> getPrimaryKey() {
		return primaryKey;
	}
	
	@Override
	public IColumnMapper getColumnMapperByFieldName(String fieldName) {
		return columnsMap.get(fieldName);
	}
	
	public Order getDefaultOrder() {
		return defaultOrder;
	}

}
