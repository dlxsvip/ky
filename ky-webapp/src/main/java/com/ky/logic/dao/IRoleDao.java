package com.ky.logic.dao;


import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.utils.page.Page;

/**
 * @author
 * @create 2017-07-06 17:25
 * @since 1.0
 **/
public interface IRoleDao {

    void createRole(UserRoleEntity role) throws Exception;

    void updateRole(UserRoleEntity role) throws Exception;

    void deleteRole(String roleId) throws Exception;

    UserRoleEntity query(String roleId) throws Exception;

    Page<UserRoleEntity> queryRolePage(UserRoleEntity role, Paging paging) throws Exception;

    UserRoleEntity getByName(String roleName);

    UserRoleEntity getByRole(String role) throws Exception;

    void deletePrivilegesByRoleId(String roleId);

    void addPrivileges(String roleId, String[] privilegeIds);
}
