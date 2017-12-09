package com.ky.logic.model.response;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/16.
 */
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String loginName;

    private String nickName;

    private String realName;

    private String cellphoneNum;

    private String telephoneNum;

    private String email;

    private String description;

    private String loginTime;

    private String createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 是否激活
     */
    private Boolean active;

    private String roles;

    /**
     * 是否已被禁闭
     */
    private Boolean forbid;

    /**
     * 禁闭时间 分钟
     */
    private Long forbidTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Boolean getForbid() {
        return forbid;
    }

    public void setForbid(Boolean forbid) {
        if (null == forbid) {
            this.forbid = false;
        } else {
            this.forbid = forbid;
        }
    }

    public Long getForbidTime() {
        return forbidTime;
    }

    public void setForbidTime(Long forbidTime) {
        this.forbidTime = forbidTime;
    }
}
