package com.ky.logic.dao.imp;

import com.ky.logic.dao.ISystemConfigDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.ConfigEntity;
import com.ky.logic.entity.SystemConfigEntity;
import com.ky.logic.type.SystemConfigType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yl on 2017/7/11.
 */
@Repository("systemConfigDao")
public class SystemConfigDao extends AbstractHibernateDao<SystemConfigEntity> implements ISystemConfigDao {

    @Override
    public SystemConfigEntity getByType(SystemConfigType configType) throws Exception {
        Session session = getCurrentSession();
        SystemConfigEntity entity = null;
        Criteria resultCr = session.createCriteria(SystemConfigEntity.class)
                .add(Restrictions.eq("type", configType));

        List<SystemConfigEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }
        return entity;
    }

    /**
     * 新增系统配置
     *
     * @param systemConfigEntity 系统配置
     * @throws Exception
     */
    @Override
    public void addConfig(SystemConfigEntity systemConfigEntity) throws Exception {
        Session session = getCurrentSession();
        session.save(systemConfigEntity);
    }

    /**
     * 更新系统配置
     *
     * @param systemConfigEntity 系统配置
     * @throws Exception
     */
    @Override
    public void updateConfig(SystemConfigEntity systemConfigEntity) throws Exception {
        Session session = getCurrentSession();
        session.update(systemConfigEntity);
    }

    /**
     * 清除系统配置
     *
     * @param systemConfigEntity 系统配置
     * @throws Exception
     */
    @Override
    public void cleanConfig(SystemConfigEntity systemConfigEntity) throws Exception {
        Session session = getCurrentSession();
        session.delete(systemConfigEntity);
    }

    /**
     * 查询系统全部配置
     *
     * @return 系统配置列表
     * @throws Exception
     */
    @Override
    public List<ConfigEntity> getConfigs() throws Exception {
        Session session = getCurrentSession();
        Criteria resultCr = session.createCriteria(ConfigEntity.class);
        return resultCr.list();
    }

    /**
     * 更具key查询配置
     *
     * @param configKey 系统配置key
     * @return 系统配置
     * @throws Exception
     */
    @Override
    public ConfigEntity getConfigByKey(String configKey) throws Exception {
        Session session = getCurrentSession();
        ConfigEntity entity = null;
        Criteria resultCr = session.createCriteria(ConfigEntity.class)
                .add(Restrictions.eq("configKey", configKey));

        List<ConfigEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }
        return entity;
    }

    /**
     * 修改系统配置
     *
     * @param config 系统配置
     * @return 配置id
     * @throws Exception
     */
    @Override
    public void updateConfig(ConfigEntity config) throws Exception {
        Session session = getCurrentSession();
        session.update(config);
    }
}
