package com.ky.logic.common.init;

import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserLoginCtrlEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.service.IPrivilegeService;
import com.ky.logic.service.IRoleService;
import com.ky.logic.service.IUserService;
import com.ky.logic.utils.BCryptUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.ScriptRunnerUtil;
import com.ky.logic.utils.WebPathUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yl on 2017/7/31.
 */
@Component
public class InitDbDate implements InitializingBean {

    @Resource(name = "privilegeService")
    private IPrivilegeService privilegeService;

    @Resource(name = "roleService")
    private IRoleService roleService;

    @Resource(name = "userService")
    private IUserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        initSql();
        //initPrivilege();
        //initRole();
        //initUser();
    }

    /**
     * 执行SQL脚本
     */
    private void initSql() {
        //System.out.println(WebPathUtil.getRootPath());
        //System.out.println(WebPathUtil.getClassRootPath());
        File file = new File(WebPathUtil.getClassRootPath() + "sql");

        if (file.isDirectory()) {
            File[] sqlFiles = file.listFiles();
            if (sqlFiles.length < 0) {
                return;
            }
            ScriptRunnerUtil.INSTANCE.executeSql(sqlFiles);
        }

    }

    /**
     * 内置默认权限
     */
    private void initPrivilege() {
        try {
            UserPrivilegeEntity userConfigPrivilege = privilegeService.getByPrivilege("USER_CONFIG");
            if (null != userConfigPrivilege) {
                return;
            }

            UserPrivilegeEntity privilege = new UserPrivilegeEntity();
            privilege.setPrivilegeName("用户管理");
            privilege.setPrivilege("USER_CONFIG");
            privilege.setDescription("拥有此权限的角色可以管理用户");

            privilegeService.createPrivilege(privilege);

            LoggerUtil.debugSysLog(this.getClass().getName(), "initPrivilege", "内置用户管理权限成功");
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "initPrivilege", "内置用户管理权限异常：" + e.getMessage());
        }
    }


    /**
     * 内置默认角色
     */
    private void initRole() {
        try {
            UserRoleEntity initRole = roleService.getByName("init");
            if (null != initRole) {
                return;
            }

            UserRoleEntity role = new UserRoleEntity();
            role.setRoleName("用户管理角色");
            role.setRole("USER_MANAGE");
            role.setDescription("此角色可以管理用户");

            Set<UserPrivilegeEntity> privilegeSet = new HashSet<>();
            UserPrivilegeEntity privilege = privilegeService.getByPrivilege("USER_CONFIG");
            privilegeSet.add(privilege);

            role.setPrivilege(privilegeSet);

            roleService.createRole(role);

            LoggerUtil.debugSysLog(this.getClass().getName(), "initRole", "内置默认角色成功");
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "initRole", "内置默认角色异常：" + e.getMessage());
        }
    }

    /**
     * 内置超级用户
     */
    private void initUser() {
        try {
            UserEntity superUser = userService.queryByName("super");
            if (null != superUser) {
                return;
            }

            String pwd = "super";
            UserEntity user = new UserEntity();
            user.setLoginName("super");
            user.setNickName("超级管理员");
            user.setDescription("系统内置");

            // 计算密码摘要
            user.setPassword(BCryptUtil.encode(pwd));

            // 添加登录策略
            UserLoginCtrlEntity userLoginCtrl = new UserLoginCtrlEntity();
            userLoginCtrl.setSystemInitPassword(pwd);
            userLoginCtrl.setConsecutiveAuthFailuresCount(0);
            user.setUserLoginCtrl(userLoginCtrl);

            // 添加默认角色
            Set<UserRoleEntity> roles = new HashSet<>();
            UserRoleEntity role = roleService.getByRole("USER_MANAGE");
            roles.add(role);
            user.setRole(roles);

            userService.createUser(user);

            LoggerUtil.debugSysLog(this.getClass().getName(), "initUser", "内置super用户成功");
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "initUser", "内置super用户异常：" + e.getMessage());
        }
    }
}
