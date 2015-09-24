/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.oracle.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.plugin.sql.GenericDAO;
import net.paoding.rose.jade.plugin.sql.annotations.Limit;
import net.paoding.rose.jade.plugin.sql.annotations.Offset;
import net.paoding.rose.jade.plugin.sql.oracle.model.Item;

/**
 * @author Alan
 *
 */
@DAO
public interface ItemDAO extends GenericDAO<Item, Long> {

	List<Item> findByName(
			@SQLParam("color") String name,
			@Offset Long offset,
			@Limit Long limit);
}
