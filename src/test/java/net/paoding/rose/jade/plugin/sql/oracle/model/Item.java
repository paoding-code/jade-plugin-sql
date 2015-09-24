/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.oracle.model;

import net.paoding.rose.jade.plugin.sql.annotations.Column;
import net.paoding.rose.jade.plugin.sql.annotations.Table;

/**
 * @author Alan
 *
 */
@Table("\"MIRACLE\".\"goods_details\"")
public class Item {
	
	@Column("\"id\"")
	private Long id;
	
	@Column("\"color\"")
	private String color;
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
