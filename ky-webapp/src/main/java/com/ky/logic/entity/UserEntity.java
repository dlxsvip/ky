package com.ky.logic.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用户表
 *
 * @author 肖振6063
 * @create 2017-07-04 13:51
 * @since 1.0
 **/
@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "pass_word")
    private String password;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "cell_phone_num")
    private String cellphoneNum;

    @Column(name = "tele_phone_num")
    private String telephoneNum;

    private String email;

    @Column(name = "create_time")
    private Date createTime;

    private String description;

    /**
     * 登录时间
     */
    @Column(name = "login_time")
    private Date loginTime;


    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 是否激活
     */
    private Boolean active;

    @OneToOne(cascade = CascadeType.ALL)
    private UserLoginCtrlEntity userLoginCtrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("roleName ASC")
    private Set<UserRoleEntity> role;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCellphoneNum() {
        return cellphoneNum;
    }

    public void setCellphoneNum(String cellphoneNum) {
        this.cellphoneNum = cellphoneNum;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public UserLoginCtrlEntity getUserLoginCtrl() {
        return userLoginCtrl;
    }

    public void setUserLoginCtrl(UserLoginCtrlEntity userLoginCtrl) {
        this.userLoginCtrl = userLoginCtrl;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<UserRoleEntity> getRole() {
        return role;
    }

    public void setRole(Set<UserRoleEntity> role) {
        this.role = role;
    }
}
