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
	
	public ParameterOriginal(Class<?> type, Annotation[] annotations) {
		this.type = type;
		this.annotations = annotations;
		
		if(annotations != null && annotations.length > 0) {
			for(Annotation annotation : annotations) {
				if(annotation.annotationType().equals(SQLParam.class)) {
					sqlParam = (SQLParam) annotation;
					break;
				}
			}
		}
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
