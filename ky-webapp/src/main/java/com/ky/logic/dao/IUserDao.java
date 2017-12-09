package com.ky.logic.dao;


import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.UserRequest;
import com.ky.logic.model.response.UserResponse;
import com.ky.logic.utils.page.Page;

import java.util.List;
import java.util.Set;

/**
 * @author yyl
 * @create 2017-07-04 16:51
 * @since 1.0
 **/
public interface IUserDao {

    void createUser(UserEntity entity) throws Exception;

    void updateUser(UserEntity entity) throws Exception;

    UserEntity queryUser(UserRequest userVo) throws  Exception;

    void deleteUser(String userId) throws  Exception;

    UserEntity query(String userId) throws Exception;

    Page<UserEntity> queryUserPage(UserRequest request)  throws  Exception;

    List<UserEntity> getAllUserSystemInitPassword(String[] ids) throws Exception;

    /**
     * 获取用户所有的角色
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    Set<UserRoleEntity> getRolesByUserId(String userId) throws Exception;

    /**
     * 获取用户所有的角色
     *
     * @param userLoginName 用户登录名
     * @return 权限列表
     * @throws Exception
     */
    Set<UserRoleEntity> getRolesByUserLoginName(String userLoginName) throws Exception;

    /**
     * 获取用户所有的权限
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    Set<UserPrivilegeEntity> getPrivilegesByUserId(String userId) throws Exception;

    /**
     * 获取用户所有的权限
     *
     * @param userLoginName 用户登录名
     * @return 权限列表
     * @throws Exception
     */
    Set<UserPrivilegeEntity> getPrivilegesByUserLoginName(String userLoginName) throws Exception;

    /**
     * 根据用户id 批量删除 用户和角色关联表数据
     *
     * @param userId 用户id
     */
    void deleteRoleByUserId(String userId);

    /**
     * 根据用户id 批量添加 用户和角色关联表数据
     *
     * @param userId  用户id
     * @param roleIds 权限ids
     */
    void addRole(String userId, String[] roleIds);

    /**
     * 查询 此角色有多少人拥有
     *
     * @param roleId 角色
     * @return 人数
     */
    Long queryUserNumByRoleId(String roleId);

    List<UserResponse> queryUsersByRoleId(String roleId, Paging paging) throws Exception;

    /**
     * 解除某个用户的禁闭时间
     *
     * @param ctrlId 用户控制策略id
     */
    void closeForbidTime(String ctrlId) throws Exception;
}
