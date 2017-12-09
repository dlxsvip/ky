package com.ky.logic.service;


import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/24.
 */
public interface IPrivilegeService {
    String createPrivilege(UserPrivilegeEntity privilege) throws Exception;

    String updatePrivilege(UserPrivilegeEntity privilege) throws Exception;

    void deletePrivilege(String privilegeId) throws Exception;

    UserPrivilegeEntity query(String privilegeId) throws Exception;

    Page<UserPrivilegeEntity> queryPrivilegePage(UserPrivilegeEntity privilege, Paging paging) throws Exception;

    UserPrivilegeEntity getByPrivilege(String privilege) throws Exception;

    UserPrivilegeEntity getPrivilegeById(String privilegeId) throws Exception;

    /**
     * 获取用户所有的权限
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    List<UserPrivilegeEntity> getByUserId(String userId) throws Exception;
}
