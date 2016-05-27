/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.paoding.rose.jade.plugin.sql.util.PlumUtils;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public abstract class AbstractMapper<O> implements IMapper<O> {
	
	protected O original;
	
	private String destName;
	
	private String originalName;
	
	public static final char SEPARATOR = '_';
	
	protected static Log logger = LogFactory.getLog(IMapper.class);
	
	public AbstractMapper(O original) {
		this.original = original;
        this.originalName = generateOriginalName();
        this.destName = generateDestName(getOriginalName());
	}
	

    @Override
    public void init() {
    }
	
	public O getOriginal() {
		return original;
	}
	
	public String getOriginalName() {
		return originalName;
	}
	
	protected String generateOriginalName() {
		return original.toString();
	}
	
	public String generateDestName(String source) {
		if(PlumUtils.isBlank(source)) {
			return null;
		}
		
		if(source.matches("^[a-zA-Z\\\\.]+$")) {
			StringBuilder result = new StringBuilder();
			
			for(int i = 0; i < source.length(); i++) {
				char c = source.charAt(i);
				
				if(Character.isWhitespace(c)) {
					continue;
				}
				
				if(Character.isUpperCase(c)) {
					if(result.length() > 0) {
						result.append(SEPARATOR);
					}
					result.append(Character.toLowerCase(c));
				} else {
					result.append(c);
				}
			}
			
			return result.toString();
		} else {
			throw new IllegalArgumentException("Illegal naming conventions.");
		}
	}

	@Override
	public String getDestName() {
		return destName;
	}

}
