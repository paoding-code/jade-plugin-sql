/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.Assert;
import net.paoding.rose.jade.plugin.sql.dao.BooleanEnum;
import net.paoding.rose.jade.plugin.sql.dao.UserInfoDAO;
import net.paoding.rose.jade.plugin.sql.model.UserInfoDO;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class PlumUpdateTestCase extends AbstractTestCase {

	@Autowired
	private UserInfoDAO userInfoDAO;
    
    public void testUpdate() {
        UserInfoDO contract = new UserInfoDO();
        
        contract.setId(1L);
        contract.setLastUpdateTime(new Date());
        contract.setName("Alan.Geng");
        
        System.out.println(userInfoDAO.update(contract));
        
        UserInfoDO user = userInfoDAO.get(1L);
        System.out.println(JSON.toJSONString(user, SerializerFeature.PrettyFormat));
    }
    
    public void testUpdateEnum() {

        userInfoDAO.updateBoolEnum(1, BooleanEnum.TRUE);

        UserInfoDO user = userInfoDAO.get(1L);
        System.out.println(JSON.toJSONString(user, SerializerFeature.PrettyFormat));
        //
        userInfoDAO.updateBoolEnum(1, BooleanEnum.FALSE);

        UserInfoDO user2 = userInfoDAO.get(1L);
        System.out.println(JSON.toJSONString(user2, SerializerFeature.PrettyFormat));
    }

    public void testBaseDAOUpdate() {
        UserInfoDO user = userInfoDAO.findAll().get(0);
        userInfoDAO.updateStatus(user.getId(), 123);
        
        int status = userInfoDAO.getStatus(user.getId());
        
        Assert.assertEquals(123, status);
        

        user = userInfoDAO.get(user.getId());
        System.out.println(JSON.toJSONString(user, SerializerFeature.PrettyFormat));
    }
    
    public void testSpecialUpdate() {
        userInfoDAO.updateByGroup(null, 25, 100);
        
        List<UserInfoDO> updated = userInfoDAO.findByGroupId(100L);
        System.out.println(JSON.toJSONString(updated, SerializerFeature.PrettyFormat));
    }
	
	public void testSpecialInUpdate() {
		List<Integer> groupIds = new ArrayList<Integer>();
		groupIds.add(100);
		groupIds.add(101);
		
		userInfoDAO.updateByGroupIds("Alan.Geng", 29, groupIds);
		
		List<UserInfoDO> updated = userInfoDAO.findByGroupIds(groupIds);
		System.out.println(JSON.toJSONString(updated, SerializerFeature.PrettyFormat));
	}
}
