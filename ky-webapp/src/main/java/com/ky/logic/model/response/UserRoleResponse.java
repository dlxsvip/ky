package com.ky.logic.model.response;


import com.ky.logic.entity.UserPrivilegeEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by yl on 2017/8/7.
 */
public class UserRoleResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String roleName;

    private String role;

    /**
     * 角色说明
     */
    private String description;

    private Date createTime;

    private Date updateTime;

    /**
     * 该角色拥有的权限
     */
    private Set<UserPrivilegeEntity> privilege;

    private Long userNum;

    private Boolean isDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<UserPrivilegeEntity> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Set<UserPrivilegeEntity> privilege) {
        this.privilege = privilege;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        if (null == isDefault) {
            this.isDefault = false;
        } else {
            this.isDefault = isDefault;
        }
    }
}
