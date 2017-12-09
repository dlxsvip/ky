package com.ky.logic.service;


import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.UserRoleRequest;
import com.ky.logic.utils.page.Page;

/**
 * @author yyl
 * @create 2017-07-06 17:30
 * @since 1.0
 **/
public interface IRoleService {
    String createRole(UserRoleEntity role) throws Exception;

    String updateRole(UserRoleRequest roleParam) throws Exception;

    void deleteRole(String roleId) throws Exception;

    UserRoleEntity query(String roleId) throws Exception;

    Page<UserRoleEntity> queryRolePage(UserRoleEntity role, Paging paging) throws Exception;

    UserRoleEntity getByName(String roleName) throws Exception;

    UserRoleEntity getByRole(String role) throws Exception;
}
