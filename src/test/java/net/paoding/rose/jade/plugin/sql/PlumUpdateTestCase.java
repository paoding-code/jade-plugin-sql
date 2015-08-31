/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
		contract.setName("Alan.Geng");
		
		System.out.println(purchaseContractDAO.update(contract));
		
		UserInfoDO user = purchaseContractDAO.get(1L);
		System.out.println(JSON.toJSONString(user, SerializerFeature.PrettyFormat));
	}
	
	public void testSpecialUpdate() {
		purchaseContractDAO.updateByGroup("Alan.Geng", 29, 100);
		
		List<UserInfoDO> updated = purchaseContractDAO.findByGroupId(100);
		System.out.println(JSON.toJSONString(updated, SerializerFeature.PrettyFormat));
	}
}
