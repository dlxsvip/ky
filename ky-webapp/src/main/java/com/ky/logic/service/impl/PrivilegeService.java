package com.ky.logic.service.impl;

import com.ky.logic.dao.IPrivilegeDao;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.service.IPrivilegeService;
import com.ky.logic.utils.IdUtil;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by yl on 2017/7/24.
 */
@Repository("privilegeService")
public class PrivilegeService implements IPrivilegeService {

    @Resource(name = "privilegeDao")
    private IPrivilegeDao privilegeDao;

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createPrivilege(UserPrivilegeEntity privilege) throws Exception {
        String privilegeId = IdUtil.getIdWorker().nextUUID();
        privilege.setId(privilegeId);
        privilege.setDefault(false);
        privilege.setCreateTime(new Date());
        privilegeDao.createPrivilege(privilege);

        return privilegeId;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updatePrivilege(UserPrivilegeEntity privilege) throws Exception {

        UserPrivilegeEntity old = privilegeDao.query(privilege.getId());
        if (null == old) {
            return "不存在";
        }

        privilege.setUpdateTime(new Date());
        if (!old.isDefault() || null == old.isDefault()) {
            old.setDefault(false);
        }

        if (StringUtils.isNotEmpty(privilege.getPrivilegeName())) {
            old.setPrivilegeName(privilege.getPrivilegeName());
        }

        if (StringUtils.isNotEmpty(privilege.getPrivilege())) {
            old.setPrivilege(privilege.getPrivilege());
        }

        if (StringUtils.isNotEmpty(privilege.getDescription())) {
            old.setDescription(privilege.getDescription());
        }


        privilegeDao.updatePrivilege(old);

        return old.getId();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePrivilege(String privilegeId) throws Exception {
        privilegeDao.deletePrivilege(privilegeId);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserPrivilegeEntity query(String privilegeId) throws Exception {
        return privilegeDao.query(privilegeId);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserPrivilegeEntity> queryPrivilegePage(UserPrivilegeEntity privilege, Paging paging) throws Exception {
        return privilegeDao.queryPrivilegePage(privilege, paging);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserPrivilegeEntity getByPrivilege(String privilege) throws Exception {
        return privilegeDao.getByPrivilege(privilege);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserPrivilegeEntity getPrivilegeById(String privilegeId) throws Exception {
        return privilegeDao.getPrivilegeById(privilegeId);
    }

    /**
     * 获取用户所有的权限
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<UserPrivilegeEntity> getByUserId(String userId) throws Exception {
        return privilegeDao.getByUserId(userId);
    }
}
