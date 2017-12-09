package com.ky.logic.service;


import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserLoginCtrlEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.UserRequest;
import com.ky.logic.model.response.UserResponse;
import com.ky.logic.utils.page.Page;

import java.util.List;
import java.util.Set;

/**
 * @author yyl
 * @create 2017-07-04 16:34
 * @since 1.0
 **/
public interface IUserService {
    String createUser(UserRequest userVo) throws Exception;

    String createUser(UserEntity user) throws Exception;

    String updateUser(UserRequest userVo) throws Exception;


    String modifyUser(UserEntity user) throws Exception;

    void deleteUser(String userId) throws  Exception;

    Page<UserEntity> queryUserPage(UserRequest request) throws Exception;

    Page<UserResponse> queryByPage(UserRequest request) throws Exception;

    UserResponse queryInfo(String userId) throws Exception;

    /**
     * 根据唯一的id查询用户
     *
     * @param userId 用户id
     * @return 用户
     * @throws Exception
     */
    UserEntity queryByUserId(String userId) throws Exception;

    /**
     * 根据唯一的名称查询用户
     *
     * @param userName 用户名
     * @return 用户
     * @throws Exception
     */
    UserEntity queryByName(String userName) throws Exception;


    /**
     * 重置密码
     *
     * @param user        被重置密码的用户
     * @param newPassword 新密码
     * @return 用户id
     * @throws Exception
     */
    String resetPassword(UserEntity user, String newPassword) throws Exception;

    /**
     * 修改密码
     *
     * @param user        当前用户
     * @param newPassword 新密码
     * @return 用户id
     * @throws Exception
     */
    String updatePassword(UserEntity user, String newPassword) throws Exception;

    List<UserEntity> getAllUserSystemInitPassword(String[] ids) throws Exception;


    /**
     * 是否有禁闭时间
     *
     * @param loginCtrl 登录策略
     * @return 禁闭剩余时间, 单位秒
     */
    long checkForbidTime(UserLoginCtrlEntity loginCtrl);

    /**
     * 密码是否到期
     * @param loginCtrl 登录策略
     * @return 是否到期
     */
    boolean isPwdExpire(UserLoginCtrlEntity loginCtrl);

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     * @throws Exception
     */
    UserEntity getCurLoginUser() throws Exception;

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

    Long queryNumByRoleId(String roleId);


    Page<UserResponse> queryUsersByRoleId(String roleId, Paging paging) throws Exception;

    /**
     * 解除某个用户的禁闭时间
     *
     * @param userId 用户id
     * @return 结果
     */
    String closeForbidTime(String userId) throws Exception;
}
