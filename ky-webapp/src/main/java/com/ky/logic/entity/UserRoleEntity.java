package com.ky.logic.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 角色表
 *
 * @author 肖振6063
 * @create 2017-07-04 14:06
 * @since 1.0
 **/
@Entity
@Table(name = "user_role")
public class UserRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role")
    private String role;

    /**
     * 角色说明
     */
    private String description;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否系统默认
     */
    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * 该角色拥有的权限
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("privilegeName ASC")
    private Set<UserPrivilegeEntity> privilege;



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

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        if (null == isDefault) {
            this.isDefault = false;
        } else {
            this.isDefault = isDefault;
        }

    }
}
