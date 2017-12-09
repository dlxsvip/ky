package com.ky.logic.model.request;


import com.ky.logic.model.Paging;

import java.io.Serializable;

/**
 * @author 肖振6063
 * @create 2017-07-05 9:52
 * @since 1.0
 **/
public class UserRequest extends Paging implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String loginName;

    private String password;

    private String nickName;

    private String realName;

    private String cellphoneNum;

    private String telephoneNum;

    private String email;

    private String description;

    private String loginTime;

    /**
     * 角色s "," 分隔
     */
    private String roleIds;

    private Boolean isSuper;

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

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public Boolean getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(Boolean isSuper) {
        if (null == isSuper) {
            this.isSuper = false;
        } else {
            this.isSuper = isSuper;
        }

    }
}
