/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import java.lang.annotation.Annotation;

import net.paoding.rose.jade.annotation.SQLParam;

/**
 * @author Alan
 *
 */
public class ParameterOriginal {
	
	private SQLParam sqlParam;
	
	private Class<?> type;
	
	private Annotation[] annotations;
	
	public ParameterOriginal(SQLParam sqlParam, Class<?> type, Annotation[] annotations) {
		this.sqlParam = sqlParam;
		this.type = type;
		this.annotations = annotations;
	}

	public Class<?> getType() {
		return type;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public SQLParam getSqlParam() {
		return sqlParam;
	}
	
	public String getName() {
		return sqlParam == null ? type.getSimpleName() :  sqlParam.value();
	}
}
