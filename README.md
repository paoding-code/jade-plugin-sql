# Welcome to `jade-plugin-sql`
jade-plugin-sql是一个 paoding-rose-jade 插件。它能够根据 DAO 方法，自动生成 SQL 语句，减少手动拼写SQL工作量。

（jade原生的@SQL方式和本插件使用的方式默认同时有效，互相不冲突）


## User Guide

### Maven Dependency
```xml
<dependency>
	<groupId>net.paoding</groupId>
	<artifactId>jade-plugin-sql</artifactId>
	<!-- A young boy! Hahaha~~ -->
	<version>0.0.1</version>
</dependency>
```

### 通过Spring配置jade-plugin-sql
```xml
    <context:annotation-config />

    <bean id="dataSource"
        class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="org.hsqldb.jdbc.JDBCDriver" />
        <property name="url" value="jdbc:hsqldb:mem:jade_plugin_sql_db" />
    </bean>

    <!-- 启用Jade配置 -->
    <bean class="net.paoding.rose.jade.context.spring.JadeBeanFactoryPostProcessor" />

    <!-- 启用jade-plugin-sql插件 -->
    <bean class="net.paoding.rose.jade.plugin.sql.PlumSQLInterpreter">
        <property name="dialect">
            <bean class="net.paoding.rose.jade.plugin.sql.dialect.MySQLDialect" />
        </property>
    </bean>
```

### 一个基本CRUD
首先我们需要一个DO对数据进行封装。
```java
/*
 *  当类名完全符合驼峰到下划线转换规则，可不使用@Table注解。
 *  XxxDO中的DO不会被转换到表明映射中，即：UserDO -> user。
 */
@Table
public class UserDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7106854911318965104L;

	// 设置主键列
	@Column(pk = true)
	private Long id;
	
	// 完全符合规则的字段也必须声明Column，告诉插件将其映射为表中的列。
	@Column
	private String name;
	
	@Column
	private Integer age;
	
	/*
	 *  实体默认排序规则：当某个对于该实体的查询操作不设定任何排序条件则按createTime降序。
	 */
	@Column(order = Direction.DESC)
	private Date createTime;
	
	/*
	 *  指定列名：当列名不符合驼峰转下划线规则使用Annotation标记。
	 */
	@Column("last_update_time")
	private Date lstUpdTime;

	// 未标注@Column的属性将不被映射为表字段。
	private Object other;

	// Getter & Setter

}
```

### 编写一个DAO。
```java
/*
 * 继承GenericDAO并使用泛型标注对应的实体类型，即拥有了基本的CRUD操作。
 * 你并不需要一个实现类，在Service层中直接使用：
 * @Resource
 * private UserDAO userDAO;
 * spring框架将为之注入一个自动生成的实现。
 */
@DAO
public interface UserDAO extends GenericDAO<UserDO, Long> {
}
```

如此以来，我们从GenericDAO上继承得到了基本的CRUD操作，但这在日常的开发工作中显然不够满足需求，本插件提供了一系列的约定，让你仅仅在接口中声明出方法，即可获得该方法的实际功能。

### 按指定字段查询：
```java
	/**
	 * 指定字段查询
	 * @param age
	 * @return
	 */
	// 无需@SQL注解手写sql语句，实际的SQL语句由jade-plugin-sql动态生成
	public List<UserDO> findByAge(@SQLParam("age") Integer age);
```

如上，当你定义了这样一个方法，则会自动在该方法被调用时生成一条SQL，并执行它，自动封装为返回值指定的结果类型：
```sql
    SELECT
        id,
        name,
        age,
        create_time,
        last_update_time
    FROM
        user 
    WHERE
        age = :age 
    ORDER BY
        -- 来自DO中的默认排序
        create_time DESC
```

### 字符串Like查询：
```java
	/**
	 * 字符串Like查询
	 * @param name 传进来的name参数自己负责百分号的拼接，比如传入"zhang%"
	 * @return
	 */
	public List<UserDO> findByName(@SQLParam("name") @Like String name);
```

### 值区间查询：
```java
	/**
	 * 值区间查询
	 * @param min
	 * @param max
	 * @return
	 */
	public List<UserDO> findByAgeBwteen(
			@SQLParam("age") @Ge Long min,
			@SQLParam("age") @Le Long max);
```

### 指定排序条件查询：
```java
	/**
	 * 排序查询
	 * @param name
	 * @param order
	 * @return
	 */
	public List<UserDO> findByNameLikeWithOrder(
			@SQLParam("name") @Like String name,
			Order order);
```
我们也简单做了一些语法糖使Order对象的创建变得方便。
```java
	userDAO.findByNameLikeWithOrder("Plum%", Plum.asc("name", "age").desc("createTime"));
```
如上所示，该方法执行时，将自动生成SQL：
```sql
    SELECT
        id,
        name,
        age,
        create_time,
        last_update_time 
    FROM
        user 
    WHERE
        name like :name 
    ORDER BY
    	-- 当指定了排序条件时，将不会使用DO中的默认排序。
        name ASC,
        age ASC,
        create_time DESC
```

### 查询记录区间：
```java
	/**
	 * Range查询
	 * @param name
	 * @param range
	 * @return
	 */
	public List<UserDAO> findWithLimit(
			@SQLParam("name") @Like String name,
			@Offset Integer start,
			@Limit Integer limit);
```
```java
	userDAO.findWithLimit("Plum%", 0, 2);
```
```sql
    SELECT
        id,
        name,
        age,
        create_time,
        last_update_time 
    FROM
        user 
    WHERE
        name like :name 
    ORDER BY
        create_time DESC
    LIMIT 0, 2
```

### IN查询
```java
	/**
	 * In查询
	 * @param groupIds
	 * @return
	 */
	public List<UserInfoDO> findByGroupIds(
			@SQLParam("groupId") @In List<Integer> groupIds);
```
```java	
	List<UserInfoDO> userInfos = userInfoDAO.findByGroupIds(groupIds);
```
```sql
    SELECT
        id,
        name,
        group_id,
        birthday,
        age,
        money,
        create_time,
        last_update_time,
        status,
        editable 
    FROM
        user_info 
    WHERE
        group_id IN (
            :groupId
        ) 
    ORDER BY
        create_time DESC
```

### 指定条件更新指定字段
```java
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
```
在被`@Where`标记前的所有参数被应用到WHERE子句，否则被应用到UPDATE子句。
```java
	userInfoDAO.updateByGroupIds("Alan.Geng", 29, groupIds);
```
```sql
    UPDATE
        user_info 
    SET
        name = :name,
        age = :age 
    WHERE
        group_id IN (
            :groupId
        )
```

### 用于参数的一些Annotation：
* Eq: 运算符"="
* Ne: 运算符"!="
* Ge: 运算符">="
* Gt: 运算符">"
* Le: 运算符"<="
* Lt: 运算符"<"
* Like: SQL关键字"LIKE"
* In: SQL关键字"IN"
* Offset: 查询记录偏移量
* Limit: 最大返回记录数

`上述的内容不同的数据库需要在方言中实现，默认给出MySQL的实现。`

你只要记住几个预定义的注解，不用写一条SQL，即可完成单表操作90%以上的需求，在这里需要特别注意的是，使用本插件时，`@SQLParam中的串应该是Bean的属性名，不能写成表的列名。` 例如:

这是对的：@SQLParam("userName")；

这是错的：@SQLParam("user_name")

尽管提供了如此丰富的查询约定规则，但无论如何是满足不了所有需求的。此时，直接使用jade的原生方式吧：

```java
	@SQL("select id, name, cola, colb from my_table where xxxx")
	public List<UserDAO> findWithSQL(xxxx);
```
