/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.plugin.sql.Order;
import net.paoding.rose.jade.plugin.sql.annotations.Ge;
import net.paoding.rose.jade.plugin.sql.annotations.IgnoreNull;
import net.paoding.rose.jade.plugin.sql.annotations.In;
import net.paoding.rose.jade.plugin.sql.annotations.Le;
import net.paoding.rose.jade.plugin.sql.annotations.Like;
import net.paoding.rose.jade.plugin.sql.annotations.Limit;
import net.paoding.rose.jade.plugin.sql.annotations.Offset;
import net.paoding.rose.jade.plugin.sql.annotations.Where;
import net.paoding.rose.jade.plugin.sql.model.UserInfoDO;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
/*
 * 继承GenericDAO并使用泛型标注对应的实体类型，即拥有了基本的CRUD操作。
 * 你并不需要一个实现类，在Service层中直接使用：
 * @Autowired
 * private UserInfoDAO userInfoDAO;
 * spring框架将自动注入一个自动生成的实现。
 */
@DAO
public interface UserInfoDAO extends BaseDAO<UserInfoDO, Long> {

	/**
	 * 指定字段查询
	 * @param contractPlanId
	 * @return
	 */
	public List<UserInfoDO> findByGroupId(
			@SQLParam("groupId") Long groupId);
	
	/**
	 * 字符串Like查询
	 * @param name
	 * @return
	 */
	public List<UserInfoDO> findByNameLike(
			@SQLParam("name") @Like String name);
	
	/**
	 * 值区间查询
	 * @param min
	 * @param max
	 * @return
	 */
	public List<UserInfoDO> findByIdBetween(
			@SQLParam("id") @Ge long min,
			@SQLParam("id") @Le long max);
	
	/**
	 * 排序查询
	 * @param name
	 * @param order
	 * @return
	 */
	public List<UserInfoDO> findByNameLikeWithOrder(
			@SQLParam("name") @Like String name,
			Order order);
	
	/**
	 * Range查询
	 * @param name
	 * @param range
	 * @return
	 */
	@IgnoreNull(false)
	public List<UserInfoDO> findByNameWithLimit(
			@SQLParam("name") @Like String name,
			@Offset int offset,
			@Limit int limit);
	
	/**
	 * In查询
	 * @param groupIds
	 * @return
	 */
	public List<UserInfoDO> findByGroupIds(
			@SQLParam("groupId") @In List<Integer> groupIds);
	
	/**
	 * 通过指定字段条件更新
	 * @param name
	 * @param group
	 */
	public void updateByGroup(
			@SQLParam("name") @IgnoreNull(false) String name,
			@SQLParam("age") Integer age,
			@Where
			@SQLParam("groupId") Integer group);
	
	/**
	 * 通过指定字段条件更新
	 * @param name
	 * @param group
	 */
	public void updateByGroupIds(
			@SQLParam("name") String name,
			@SQLParam("age") int age,
			@Where
			@SQLParam("groupId") @In List<Integer> group);
	
	/**
	 * 通过指定字段删除
	 * @param groupId
	 */
	public void deleteByGroupId(
			@SQLParam("groupId") Integer groupId);
	
	/**
	 * 测试更新枚举类型（实际存储枚举类型的name值）
	 * @param id
	 * @param bool
	 * @return
	 */
	@SQL("update {table_name} set bool_enum=:2 where id=:1")
	public boolean updateBoolEnum(int id, BooleanEnum bool);
	
	@SQL("select * from  {table_name} ")
	public List<UserInfoDO> findAll();
	
	public Long countByGroupId(@SQLParam("groupId") Integer groupId);
}
