package com.ky.logic.service;


import com.ky.logic.entity.ConfigEntity;
import com.ky.logic.entity.SystemConfigEntity;
import com.ky.logic.model.OssConfigModel;
import com.ky.logic.type.SystemConfigType;

import java.util.List;

/**
 * Created by yl on 2017/7/11.
 */
public interface ISystemConfigService {


    /**
     * 获取OSS配置
     *
     * @return OSS配置
     */
    OssConfigModel getConfigOss() throws Exception;


    /**
     * 获取系统配置
     *
     * @param type 类型
     * @return 配置
     */
    SystemConfigEntity getSystemConfigEntity(SystemConfigType type) throws Exception;


    /**
     * 新增系统配置
     *
     * @param config 系统配置
     * @param type   类型
     */
    void addConfig(String config, SystemConfigType type) throws Exception;


    /**
     * 更新系统配置
     *
     * @param systemConfigEntity 系统配置
     */
    void updateConfig(SystemConfigEntity systemConfigEntity) throws Exception;

    /**
     * 清除系统配置
     *
     * @param type 类型
     */
    void cleanConfig(SystemConfigType type) throws Exception;

    /**
     * 查询系统全部配置
     *
     * @return 系统全部配置
     * @throws Exception
     */
    List<ConfigEntity> getConfigs() throws Exception;

    /**
     * 修改系统配置
     *
     * @param config 系统配置
     * @return 配置id
     * @throws Exception
     */
    String updateConfig(ConfigEntity config) throws Exception;

    /**
     * 批量系统配置
     *
     * @param configs 系统配置
     * @return 配置id
     * @throws Exception
     */
    String updateAllConfig(List<ConfigEntity> configs) throws Exception;

    /**
     * 根据key查询配置
     *
     * @param configKey 系统配置key
     * @return 系统配置
     * @throws Exception
     */
    ConfigEntity getConfigByKey(String configKey) throws Exception;
}
