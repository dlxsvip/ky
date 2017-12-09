package com.ky.logic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统配置参数
 * Created by yl on 2017/8/14.
 */
@Entity
@Table(name = "config")
public class ConfigEntity {

    /**
     * 配置id
     */
    @Id
    @Column(name = "config_id")
    private String configId;

    /**
     * 配置名称
     */
    @Column(name = "config_name")
    private String configName;

    /**
     * 配置key
     */
    @Column(name = "config_key")
    private String configKey;

    /**
     * 配置value
     */
    @Column(name = "config_value")
    private String configValue;

    /**
     * 配置value
     */
    @Column(name = "value_type")
    private String valueType;

    /**
     * 配置描述
     */
    private String description;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
