/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.io.Serializable;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public interface Entity extends Serializable {
	
	public Long getId();
	
	public void setId(Long id);

}
