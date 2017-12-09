package com.ky.logic.utils;

import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.service.IUserService;
import com.ky.logic.type.PrivilegeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yl on 2017/8/2.
 */
@Component
public class PrivilegeUtil {


    private static IUserService userService;


    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private static boolean privilegeInList(Set<UserPrivilegeEntity> privileges, PrivilegeType privilege) {
        for (UserPrivilegeEntity entity : privileges) {
            if (StringUtils.equals(entity.getPrivilege(), privilege.name())) {
                return true;
            }
        }

        return false;
    }

    private static Set<UserPrivilegeEntity> getCurrentUserPrivileges() {
        Set<UserPrivilegeEntity> privilegeEntities = new HashSet<>();
        try {
            UserEntity user = userService.getCurLoginUser();
            if (null == user) {
                return privilegeEntities;
            }

            privilegeEntities = userService.getPrivilegesByUserId(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return privilegeEntities;
    }

    private static boolean userHasPrivilege(PrivilegeType privilege) {
        return privilegeInList(getCurrentUserPrivileges(), privilege);
    }

    /**
     * 是否具有权限
     * @param type 权限
     * @return 是 or 否
     */
    public static boolean HasPrivilege(PrivilegeType type) {
        return userHasPrivilege(type);
    }

    /**
     * 是否具有用户管理权限
     *
     * @return 是 or 否
     */
    public static boolean HasUserConfig() {
        return userHasPrivilege(PrivilegeType.USER_CONFIG);
    }

    /**
     * 是否具有系统配置权限
     *
     * @return 是 or 否
     */
    public static boolean HasSystemConfig() {
        return userHasPrivilege(PrivilegeType.SYSTEM_CONFIG);
    }

    /**
     * 是否具有信息注册权限
     *
     * @return 是 or 否
     */
    public static boolean HasMsgRegister() {
        return userHasPrivilege(PrivilegeType.MSG_REGISTER);
    }

    /**
     * 是否具有视频查看权限
     *
     * @return 是 or 否
     */
    public static boolean HasVideoResult() {
        return userHasPrivilege(PrivilegeType.VIDEO_RESULT);
    }
}
