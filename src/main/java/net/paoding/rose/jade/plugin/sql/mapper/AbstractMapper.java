/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.mapper;

import net.paoding.rose.jade.plugin.sql.util.MyLangUtils;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public abstract class AbstractMapper<O> implements IMapper<O> {
	
	protected O original;
	
	private String name;
	
	private String originalName;
	
	private boolean mapped;
	
	public static final char SEPARATOR = '_';
	
	public AbstractMapper(O original) {
		this.original = original;
	}

	@Override
	public void map() {
		if(!mapped) {
			this.originalName = generateOriginalName();
			this.name = generateName(getOriginalName());
			doMap();
			mapped = true;
		}
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
	
	public String generateName(String source) {
		if(MyLangUtils.isBlank(source)) {
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
	
	protected void doMap() {
		
	};

	public String getName() {
		return name;
	}

}
