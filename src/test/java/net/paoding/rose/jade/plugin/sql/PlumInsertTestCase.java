/**
 * 
 */
package net.paoding.rose.jade.plugin.sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.paoding.rose.jade.plugin.sql.dao.UserInfoDAO;
import net.paoding.rose.jade.plugin.sql.model.UserInfoDO;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class PlumInsertTestCase extends AbstractTestCase {
	
	
	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testInsert() {
		UserInfoDO userInfo = createUserInfoDO(102, 30);
		
		userInfoDAO.save(userInfo);
	}
	
	public void testBatchInsert() {
		List<UserInfoDO> userInfos = new ArrayList<UserInfoDO>(5);
		for(int i = 0; i < 5; i++) {
			userInfos.add(createUserInfoDO(103, 40 + i));
		}
		userInfoDAO.save(userInfos);
	}
	
    private static UserInfoDO createUserInfoDO(long group, int age) {
        UserInfoDO userInfo = new UserInfoDO();

        userInfo.setName("耿直PlumInsertTestCase");
        userInfo.setMoney(new BigDecimal("123456"));
        userInfo.setEditable(true);
        userInfo.setGroupId(group);
        userInfo.setAge(age);
        userInfo.setBirthday(DateUtils.addYears(new Date(), -userInfo.getAge()));
        userInfo.setStatus(2);
        userInfo.setCreateTime(new Date());
        userInfo.setEditable(true);
        userInfo.setLastUpdateTime(new Date());

        return userInfo;
    }
	
}
