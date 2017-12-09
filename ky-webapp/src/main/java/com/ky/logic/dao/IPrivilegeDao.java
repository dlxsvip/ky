package com.ky.logic.dao;


import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * @author
 * @create 2017-07-04 16:51
 * @since 1.0
 */
public interface IPrivilegeDao {

    void createPrivilege(UserPrivilegeEntity privilege) throws Exception;

    void updatePrivilege(UserPrivilegeEntity privilege) throws Exception;

    void deletePrivilege(String privilegeId) throws Exception;

    UserPrivilegeEntity query(String privilegeId) throws Exception;

    Page<UserPrivilegeEntity> queryPrivilegePage(UserPrivilegeEntity privilege, Paging paging) throws Exception;

    UserPrivilegeEntity getByPrivilege(String privilege);

    UserPrivilegeEntity getPrivilegeById(String privilegeId);
    /**
     * 获取用户所有的权限
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    List<UserPrivilegeEntity> getByUserId(String userId) throws Exception;
}
