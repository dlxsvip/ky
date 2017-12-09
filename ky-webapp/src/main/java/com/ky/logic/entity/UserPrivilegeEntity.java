package com.ky.logic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 权限表
 * Created by yl on 2017/7/24.
 */
@Entity
@Table(name = "user_privilege")
public class UserPrivilegeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;


    /**
     * 权限名称
     */
    @Column(name = "privilege_name")
    private String privilegeName;

    /**
     * 权限
     */
    private String privilege;

    /**
     * 权限描述
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
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
