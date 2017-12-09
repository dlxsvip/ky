package com.ky.logic.service.impl;

import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.dao.ISystemConfigDao;
import com.ky.logic.entity.ConfigEntity;
import com.ky.logic.entity.SystemConfigEntity;
import com.ky.logic.model.OssConfigModel;
import com.ky.logic.service.ISystemConfigService;
import com.ky.logic.type.SystemConfigType;
import com.ky.logic.utils.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yl on 2017/7/11.
 */
@Service("SystemConfigService")
public class SystemConfigService implements ISystemConfigService {

    @Resource(name = "systemConfigDao")
    private ISystemConfigDao systemConfigDao;

    /**
     * 获取OSS配置
     *
     * @return OSS配置
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OssConfigModel getConfigOss() throws Exception {
        SystemConfigEntity systemConfigEntity = getSystemConfigEntity(SystemConfigType.OSSStorage);
        if (null == systemConfigEntity) {
            return null;
        }

        String config = systemConfigEntity.getConfig();
        if (null == config) {
            return null;
        }

        OssConfigModel ossConfigModel = new OssConfigModel();

        JSONObject object = new JSONObject(config);
        if (object.has("endPoint")) {
            ossConfigModel.setEndpoint(object.get("endPoint").toString());
        }
        if (object.has("bucketName")) {
            ossConfigModel.setBucketname(object.get("bucketName").toString());
        }
        if (object.has("accesskeyID")) {
            ossConfigModel.setAccesskeyID(object.get("accesskeyID").toString());
        }
        if (object.has("accesskeyKey")) {
            ossConfigModel.setAccesskeyKey(object.get("accesskeyKey").toString());
        }

        return ossConfigModel;
    }

    /**
     * 获取系统配置
     *
     * @param type 类型
     * @return 配置
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SystemConfigEntity getSystemConfigEntity(SystemConfigType type) throws Exception {
        return systemConfigDao.getByType(type);
    }

    /**
     * 新增系统配置
     *
     * @param config 系统配置
     * @param type   类型
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addConfig(String config, SystemConfigType type) throws Exception {
        SystemConfigEntity systemConfigEntity = new SystemConfigEntity();
        systemConfigEntity.setType(type);
        systemConfigEntity.setConfig(config);

        systemConfigDao.addConfig(systemConfigEntity);
    }

    /**
     * 更新系统配置
     *
     * @param systemConfigEntity 系统配置
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateConfig(SystemConfigEntity systemConfigEntity) throws Exception {
        systemConfigDao.updateConfig(systemConfigEntity);
    }

    /**
     * 清除系统配置
     *
     * @param type 类型
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cleanConfig(SystemConfigType type) throws Exception {
        SystemConfigEntity systemConfigEntity = getSystemConfigEntity(type);
        if (null == systemConfigEntity) {
            return;
        }

        systemConfigDao.cleanConfig(systemConfigEntity);
    }

    /**
     * 查询系统全部配置
     *
     * @return 系统全部配置
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ConfigEntity> getConfigs() throws Exception {
        return systemConfigDao.getConfigs();
    }

    /**
     * 修改系统配置
     *
     * @param configVo 系统配置参数
     * @return 配置id
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updateConfig(ConfigEntity configVo) throws Exception {
        ConfigEntity configEntity = systemConfigDao.getConfigByKey(configVo.getConfigKey());
        if (null == configEntity) {
            return "不存在";
        }

        configEntity = mergeEntity(configEntity, configVo);

        // 更新数据库
        systemConfigDao.updateConfig(configEntity);

        // 更新缓存
        SystemCache.getInstance().updateByKey(configEntity.getConfigKey(), configEntity.getConfigValue());

        return configEntity.getConfigId();
    }

    /**
     * 批量系统配置
     *
     * @param configs 系统配置
     * @return 配置id
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updateAllConfig(List<ConfigEntity> configs) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (ConfigEntity vo : configs) {
            try {
                String configId = updateConfig(vo);
                sb.append(configId).append(",");
            } catch (Exception e) {
                LoggerUtil.errorSysLog(this.getClass().getName(), "updateAllConfig", "修改系统配置异常," + e.getMessage());
            }
        }

        return sb.toString();
    }

    /**
     * 根据key查询配置
     *
     * @param configKey 系统配置key
     * @return 系统配置
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ConfigEntity getConfigByKey(String configKey) throws Exception {
        return systemConfigDao.getConfigByKey(configKey);
    }

    private ConfigEntity mergeEntity(ConfigEntity configEntity, ConfigEntity configVo) {

        if (StringUtils.isNotEmpty(configVo.getConfigName())) {
            configEntity.setConfigName(configVo.getConfigName());
        }

        if (StringUtils.isNotEmpty(configVo.getConfigValue())) {
            configEntity.setConfigValue(configVo.getConfigValue());
        }

        if (StringUtils.isNotEmpty(configVo.getDescription())) {
            configEntity.setDescription(configVo.getDescription());
        }

        if (StringUtils.isNotEmpty(configVo.getValueType())) {
            configEntity.setValueType(configVo.getValueType());
        }

        return configEntity;
    }
}
