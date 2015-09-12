/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.paoding.rose.jade.plugin.sql.Plum;
import net.paoding.rose.jade.plugin.sql.dao.UserInfoDAO;
import net.paoding.rose.jade.plugin.sql.model.UserInfoDO;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class PlumSelectTestCase extends AbstractTestCase {
	
	@Autowired
	private UserInfoDAO userInfoDAO;

	public void testGetById() {
		UserInfoDO userInfoDO = userInfoDAO.get(2L);
		
		assert userInfoDO != null;
		System.out.println(JSON.toJSONString(userInfoDO, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByGroupId() {
		List<UserInfoDO> userInfos = userInfoDAO.findByGroupId(100L);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByNameLike() {
		List<UserInfoDO> userInfos = userInfoDAO.findByNameLike("耿%");
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByIdBetween() {
		List<UserInfoDO> userInfos = userInfoDAO.findByIdBetween(0, 9);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testGetOrderBy() {
		List<UserInfoDO> userInfos = userInfoDAO.findByNameLikeWithOrder(//
		    "耿%", Plum.asc("id", "createTime").desc("lastUpdateTime"));
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testGetRange() {
		List<UserInfoDO> userInfos = userInfoDAO.findByNameWithLimit("耿%", 0, 2);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByGroupIds() {
		List<Integer> groupIds = new ArrayList<Integer>();
		groupIds.add(100);
		groupIds.add(101);
		
		List<UserInfoDO> userInfos = userInfoDAO.findByGroupIds(groupIds);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testCountByGroupId() {
		System.out.println(userInfoDAO.countByGroupId(100));
	}
	
}
