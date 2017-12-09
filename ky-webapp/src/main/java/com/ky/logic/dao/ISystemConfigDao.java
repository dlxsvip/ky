package com.ky.logic.dao;

import com.ky.logic.entity.ConfigEntity;
import com.ky.logic.entity.SystemConfigEntity;
import com.ky.logic.type.SystemConfigType;

import java.util.List;

/**
 * Created by yl on 2017/7/11.
 */
public interface ISystemConfigDao {

    /**
     * 获取系统配置
     *
     * @param configType 类型
     * @return 配置
     */
    SystemConfigEntity getByType(SystemConfigType configType)  throws Exception;

    /**
     * 新增系统配置
     *
     * @param systemConfigEntity 系统配置
     * @throws Exception
     */
    void addConfig(SystemConfigEntity systemConfigEntity) throws Exception;

    /**
     * 更新系统配置
     *
     * @param systemConfigEntity 系统配置
     * @throws Exception
     */
    void updateConfig(SystemConfigEntity systemConfigEntity) throws Exception;


    /**
     * 清除系统配置
     *
     * @param systemConfigEntity 系统配置
     * @throws Exception
     */
    void cleanConfig(SystemConfigEntity systemConfigEntity) throws Exception;

    /**
     * 查询系统全部配置
     *
     * @return 系统配置列表
     * @throws Exception
     */
    List<ConfigEntity> getConfigs() throws Exception;

    /**
     * 更具key查询配置
     *
     * @param configKey 系统配置key
     * @return 系统配置
     * @throws Exception
     */
    ConfigEntity getConfigByKey(String configKey) throws Exception;

    /**
     * 修改系统配置
     *
     * @param config 系统配置
     * @return 配置id
     * @throws Exception
     */
    void updateConfig(ConfigEntity config) throws Exception;
}
