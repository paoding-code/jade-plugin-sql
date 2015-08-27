/**
 * 
 */
package net.paoding.rose.jade.plugin.sql.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import net.paoding.rose.jade.plugin.sql.Order.Direction;
import net.paoding.rose.jade.plugin.sql.annotations.Column;
import net.paoding.rose.jade.plugin.sql.annotations.Table;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
/*
 *  当类名完全符合驼峰到下划线转换规则，可不使用@Table注解。
 *  XxxDO中的DO不会被转换到表明映射中，即：UserInfoDO -> user_info。
 */
@Table
public class UserInfoDO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 12345678765432L;

    // 设置主键列
    @Column(pk = true)
    private Long id;

    @Column
    private String name;

    @Column
    private Long groupId;

    @Column
    private Date birthday;

    @Column
    private Integer age;

    @Column
    private BigDecimal money;

    // 实体默认排序规则：当某个对于该实体的查询操作不设定任何排序条件则按createTime降序。
    @Column(order = Direction.DESC)
    private Date createTime;

    @Column // 默认映射的列名是last_update_time
    private Date lastUpdateTime;

    @Column
    private Integer status;

    @Column
    private Boolean editable;

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
