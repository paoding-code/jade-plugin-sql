# Welcome to `jade-plugin-sql`
jade-plugin-sql是一个 paoding-rose-jade 插件。它能够根据 DAO 方法，自动生成 SQL 语句，减少手动拼写SQL工作量。

## User Guide

### Maven Dependency
```xml
<dependency>
	<groupId>com.cainiao.cnd</groupId>
	<artifactId>cnd-commons-jade-plugins</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

### 通过Spring配置Plum
```xml
    <context:annotation-config />

    <bean id="dataSource"
        class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="org.hsqldb.jdbc.JDBCDriver" />
        <property name="url" value="jdbc:hsqldb:mem:jade_plugin_sql_db" />
    </bean>

    <!-- Jade -->
    <bean class="net.paoding.rose.jade.context.spring.JadeBeanFactoryPostProcessor" />

	  <!-- Plum -->
    <bean class="net.paoding.rose.jade.plugin.sql.PlumSQLInterpreter">
        <property name="dialect">
            <bean class="net.paoding.rose.jade.plugin.sql.dialect.MySQLDialect" />
        </property>
        <property name="operationMapperManager">
            <bean class="net.paoding.rose.jade.plugin.sql.mapper.OperationMapperManager">
                <property name="entityMapperManager">
                    <bean class="net.paoding.rose.jade.plugin.sql.mapper.EntityMapperManager" />
                </property>
            </bean>
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
public class UserDO implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7106854911318965104L;

	// 设置主键列
	@Column(pk = true)
	private Long id;
	
	// 完全符合规则的字段也必须声明Column，告诉Plum将其映射为表中的列。
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
public interface UserDAO extends GenericDAO<UserDO> {
}
```

如此以来，我们从GenericDAO上继承得到了基本的CRUD操作，但这在日常的开发工作中显然不够满足需求，Plum提供了一系列的约定，让你仅仅在接口中声明出方法，即可获得该方法的实际功能。

### 按制定字段查询：
```java
	/**
	 * 指定字段查询
	 * @param age
	 * @return
	 */
	@SQL(Plum.R)
	public List<UserDO> findByAge(
			@SQLParam("age") Integer age);
```

如上，当你定义了这样一个方法，Plum则会自动在该方法被调用时生成一条SQL，并执行它，自动封装为返回值指定的结果类型：
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
	 * @param name
	 * @return
	 */
	@SQL(Plum.R)
	public List<UserDO> findByName(
			@SQLParam("name") @Like String name);
```

### 值区间查询：
```java
	/**
	 * 值区间查询
	 * @param min
	 * @param max
	 * @return
	 */
	@SQL(Plum.R)
	public List<UserDO> findByAgeRegion(
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
	@SQL(Plum.R)
	public List<UserDO> findOrderBy(
			@SQLParam("name") @Like String name,
			Order order);
```
我们也简单做了一些语法糖使Order对象的创建变得方便。
```java
	userDAO.findOrderBy("Plum%", Plum.asc("name", "age").desc("createTime"));
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
	@SQL(Plum.R)
	public List<PurchaseContractDO> findRange(
			@SQLParam("name") @Like String name,
			@Offset Integer start,
			@Limit Integer limit);
```
```java
	userDAO.findRange("Plum%", 0, 2);
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
你尽管记住几个Plum定义的，通俗易懂的Annotation，不用写一条SQL，即可完成单表操作90%以上的需求，在这里需要特别注意的是，`Plum中，你所指定的任何字段名，都需使用Class的属性名，数据库表列名不可使用。`
尽管提供了如此丰富的查询约定规则，实际项目中也会出现各种各样多表关联查询的需求，Plum是无法支持的，请使用Jade原生的方式。
