/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import javax.annotation.Resource;

import net.paoding.rose.jade.plugin.sql.dao.UserInfoDAO;

/**
 * @author Alan
 *
 */
public class PlumDeleteTestCase extends AbstractTestCase {
	
	@Resource
	private UserInfoDAO userInfoDAO;

	public void testDelete() {
		userInfoDAO.delete(0l);
	}
}
