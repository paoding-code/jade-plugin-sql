/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.util.List;

import net.paoding.rose.jade.plugin.sql.oracle.dao.ItemDAO;
import net.paoding.rose.jade.plugin.sql.oracle.model.Item;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

/**
 * @author Alan
 *
 */
public class PlumOracleSelectTestCase extends AbstractTestCase {
	
	@Autowired
	private ItemDAO itemDAO;

	public void testFindByName() {
		String name = "%可乐%";
		List<Item> items = itemDAO.findByName(name, 10L, 10L);
		
		System.out.println(JSON.toJSONString(items));
	}
}
