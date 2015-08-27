/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import net.paoding.rose.jade.plugin.sql.dao.UserInfoDAO;
import net.paoding.rose.jade.plugin.sql.model.UserInfoDO;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class PlumUpdateTestCase extends AbstractTestCase {

	@Autowired
	private UserInfoDAO purchaseContractDAO;
	
	public void testUpdate() {
		UserInfoDO contract = new UserInfoDO();
		
		contract.setId(1L);
		contract.setLastUpdateTime(new Date());
		
		System.out.println(purchaseContractDAO.update(contract));
	}
}
