/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class MappingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7898832991567543428L;
	
	public MappingException(String message) {
		super(message);
	}

	public MappingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
