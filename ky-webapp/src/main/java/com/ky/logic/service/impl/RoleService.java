package com.ky.logic.service.impl;

import com.ky.logic.dao.IRoleDao;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.UserRoleRequest;
import com.ky.logic.service.IRoleService;
import com.ky.logic.utils.IdUtil;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author yyl
 * @create 2017-07-06 17:30
 * @since 1.0
 **/
@Repository("roleService")
public class RoleService implements IRoleService {

    @Resource(name = "roleDao")
    private IRoleDao roleDao;

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createRole(UserRoleEntity role) throws Exception {
        String roleId = IdUtil.getIdWorker().nextUUID();
        role.setId(roleId);
        role.setCreateTime(new Date());
        role.setDefault(false);
        roleDao.createRole(role);

        return roleId;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updateRole(UserRoleRequest roleParam) throws Exception {
        UserRoleEntity role = roleDao.query(roleParam.getRoleId());
        if (null == role) {
            return "不存在";
        }

        String roleId = role.getId();
        role = mergeEntity(role, roleParam);
        roleDao.updateRole(role);

        // 权限有变动
        if (StringUtils.isNotEmpty(roleParam.getPrivileges())) {
            String[] privilegeIds = roleParam.getPrivileges().split(",");
            // 删除旧权限
            roleDao.deletePrivilegesByRoleId(roleId);

            // 添加新权限
            roleDao.addPrivileges(roleId, privilegeIds);
        }

        return roleId;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteRole(String roleId) throws Exception {
        roleDao.deleteRole(roleId);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserRoleEntity query(String roleId) throws Exception {
        return roleDao.query(roleId);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserRoleEntity> queryRolePage(UserRoleEntity role, Paging paging) throws Exception {
        return roleDao.queryRolePage(role, paging);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserRoleEntity getByName(String roleName) throws Exception {
        return roleDao.getByName(roleName);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserRoleEntity getByRole(String role) throws Exception {
        return roleDao.getByRole(role);
    }

    private UserRoleEntity mergeEntity(UserRoleEntity role, UserRoleRequest roleParam) {
        role.setUpdateTime(new Date());

        if (!role.isDefault() || null == role.isDefault()) {
            role.setDefault(false);
        }

        if (StringUtils.isNotEmpty(roleParam.getRoleName())) {
            role.setRoleName(roleParam.getRoleName());
        }

        if (StringUtils.isNotEmpty(roleParam.getDescription())) {
            role.setDescription(roleParam.getDescription());
        }


        return role;
    }
}
