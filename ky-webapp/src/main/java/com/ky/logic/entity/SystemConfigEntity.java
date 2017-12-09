package com.ky.logic.entity;


import com.ky.logic.type.SystemConfigType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 系统配置
 * Created by yl on 2017/7/11.
 */
@Entity(name = "system_config")
public class SystemConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 配置类型
     */
    @Id
    @Enumerated(EnumType.STRING)
    private SystemConfigType type;

    /**
     * 配置json文本
     */
    private String config;


    public SystemConfigType getType() {
        return type;
    }

    public void setType(SystemConfigType type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
