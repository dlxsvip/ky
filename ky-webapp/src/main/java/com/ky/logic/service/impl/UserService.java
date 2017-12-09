package com.ky.logic.service.impl;

import com.ky.logic.common.cache.CurLoginUserCache;
import com.ky.logic.common.cache.SystemCache;
import com.ky.logic.dao.IUserDao;
import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserLoginCtrlEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.UserRequest;
import com.ky.logic.model.response.UserResponse;
import com.ky.logic.service.IRoleService;
import com.ky.logic.service.IUserService;
import com.ky.logic.utils.BCryptUtil;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.IdUtil;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author yyl
 * @create 2017-07-04 16:35
 * @since 1.0
 **/
@Service(value = "userService")
public class UserService implements IUserService {

    @Resource(name = "userDao")
    private IUserDao userDao;

    @Resource(name = "roleService")
    private IRoleService roleService;

    /**
     * 创建用户 提供给前台调用
     *
     * @param userVo 用户参数
     * @return 异常
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createUser(UserRequest userVo) throws Exception {
        UserEntity user = request2entity(userVo);

        return createUser(user);
    }

    /**
     * 创建用户
     *
     * @param user 用户
     * @return 异常
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createUser(UserEntity user) throws Exception {
        String userId = IdUtil.getIdWorker().nextUUID();
        user.setId(userId);
        user.setCreateTime(new Date());
        user.setActive(true);
        user.setDeleted(false);
        userDao.createUser(user);

        return userId;
    }


    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updateUser(UserRequest userVo) throws Exception {
        UserEntity user = userDao.queryUser(userVo);
        if (null == user) {
            return "不存在";
        }
        String userId = user.getId();
        user = mergeEntity(user, userVo);
        userDao.updateUser(user);

        // 角色有变动
        if (StringUtils.isNotEmpty(userVo.getRoleIds())) {
            String[] roleIds = userVo.getRoleIds().split(",");

            // 删除旧角色
            userDao.deleteRoleByUserId(userId);

            // 添加新角色
            userDao.addRole(userId, roleIds);
        }

        return userId;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String modifyUser(UserEntity user) throws Exception {
        userDao.updateUser(user);

        return user.getId();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteUser(String userId) throws Exception {
        userDao.deleteUser(userId);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserEntity> queryUserPage(UserRequest request) throws Exception {

        return userDao.queryUserPage(request);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserResponse> queryByPage(UserRequest request) throws Exception {
        Page<UserEntity> pageEntity = userDao.queryUserPage(request);


        return entity2response(pageEntity);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserResponse queryInfo(String userId) throws Exception {
        UserEntity user = userDao.query(userId);
        if (null == user) {
            return null;
        }


        return entity2response(user);
    }

    /**
     * 根据唯一的id查询用户
     *
     * @param userId 用户id
     * @return 用户
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserEntity queryByUserId(String userId) throws Exception {
        return userDao.query(userId);
    }

    /**
     * 根据唯一的名称查询用户
     *
     * @param userName 用户名
     * @return 用户
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserEntity queryByName(String userName) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }

        UserRequest request = new UserRequest();
        request.setLoginName(userName);

        return userDao.queryUser(request);
    }

    /**
     * 重置密码
     *
     * @param user        被重置密码的用户
     * @param newPassword 新密码
     * @return 用户id
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String resetPassword(UserEntity user, String newPassword) throws Exception {
        Integer length = SystemCache.getInstance().getInteger("password.init.length");

        if (StringUtils.isEmpty(newPassword)) {
            newPassword = UUID.randomUUID().toString().substring(0, length);
        }

        /* 记录系统初始密码 */
        UserLoginCtrlEntity userLoginCtrl = user.getUserLoginCtrl();
        if (userLoginCtrl == null) {
            userLoginCtrl = new UserLoginCtrlEntity();
        }
        userLoginCtrl.setLastPwdModifyTime(null);
        userLoginCtrl.setSystemInitPassword(newPassword);
        userLoginCtrl.setConsecutiveAuthFailuresCount(0);
        // 提示修改默认密码
        userLoginCtrl.setIsNeedToLoginAas(true);


        /* 密码需要计算摘要，只存储摘要 */
        user.setPassword(BCryptUtil.encode(newPassword));

        // 更新
        userDao.updateUser(user);

        return user.getId();
    }


    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String updatePassword(UserEntity user, String newPassword) throws Exception {
        Integer length = SystemCache.getInstance().getInteger("password.init.length");

        if (StringUtils.isEmpty(newPassword)) {
            newPassword = UUID.randomUUID().toString().substring(0, length);
        }

        /* 修改属性 */
        UserLoginCtrlEntity userLoginCtrl = user.getUserLoginCtrl();
        if (userLoginCtrl == null) {
            userLoginCtrl = new UserLoginCtrlEntity();
        }

        // 密码修改时间
        userLoginCtrl.setLastPwdModifyTime(new Timestamp(System.currentTimeMillis()));
        // 失败次数置零
        userLoginCtrl.setConsecutiveAuthFailuresCount(0);
        // 不进入提示修改初始密码页面
        userLoginCtrl.setIsNeedToLoginAas(false);
        // 清空初始密码
        userLoginCtrl.setSystemInitPassword(null);

        /* 密码需要计算摘要，只存储摘要 */
        user.setPassword(BCryptUtil.encode(newPassword));

        // 更新
        userDao.updateUser(user);

        return user.getId();
    }



    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<UserEntity> getAllUserSystemInitPassword(String[] ids) throws Exception {
        return userDao.getAllUserSystemInitPassword(ids);
    }


    /**
     * 是否有禁闭时间
     *
     * @param loginCtrl 登录策略
     * @return 禁闭剩余时间, 单位秒
     */
    @Override
    public long checkForbidTime(UserLoginCtrlEntity loginCtrl) {
        Timestamp attTime = loginCtrl.getLastAttemptLoginTime();
        if (null == attTime) {
            return 0;
        }

        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        if (curTime.compareTo(attTime) > 0) {
            return 0;
        }

        return attTime.getTime() - curTime.getTime();
    }

    /**
     * 密码是否到期
     *
     * @param loginCtrl 登录策略
     * @return 是否到期
     */
    @Override
    public boolean isPwdExpire(UserLoginCtrlEntity loginCtrl) {
        boolean b = false;
        Timestamp lastPwdModifyTime = loginCtrl.getLastPwdModifyTime();

        if (null == lastPwdModifyTime) {
            return false;
        }

        // 密码有效期
        Integer passwordValidDays = SystemCache.getInstance().getInteger("password.valid.days");
        Integer pwdExpiringDays = calculatePasswordExpiringDays(lastPwdModifyTime, passwordValidDays);

        if (pwdExpiringDays <= 0) {
            b = true;
        }

        return b;
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     * @throws Exception
     */
    @Override
    public UserEntity getCurLoginUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetail = (UserDetails) principal;
        String userName = userDetail.getUsername();
        UserEntity user = CurLoginUserCache.getUser(userName);
        if (null != user) {
            return user;
        }

        // 数据库查询
        UserRequest request = new UserRequest();
        request.setLoginName(userName);
        user = userDao.queryUser(request);
        // 放入缓存
        CurLoginUserCache.putUser(user.getLoginName(), user);

        return user;
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
    public Set<UserPrivilegeEntity> getPrivilegesByUserId(String userId) throws Exception {
        return userDao.getPrivilegesByUserId(userId);
    }

    /**
     * 获取用户所有的权限
     *
     * @param userLoginName 用户登录名
     * @return 权限列表
     * @throws Exception
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Set<UserPrivilegeEntity> getPrivilegesByUserLoginName(String userLoginName) throws Exception {
        return userDao.getPrivilegesByUserLoginName(userLoginName);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long queryNumByRoleId(String roleId) {
        return userDao.queryUserNumByRoleId(roleId);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserResponse> queryUsersByRoleId(String roleId,Paging paging) throws Exception {
        Page<UserResponse> page = new Page<>();

        Long count = userDao.queryUserNumByRoleId(roleId);
        page.setTotalRows(count);
        if (0 == count) {
            return page;
        }

        List<UserResponse> list = userDao.queryUsersByRoleId(roleId, paging);
        page.setResult(list);

        return page;
    }

    /**
     * 解除某个用户的禁闭时间
     *
     * @param userId 用户id
     * @return 结果
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String closeForbidTime(String userId) throws Exception {
        UserEntity userEntity = queryByUserId(userId);
        if(null == userEntity){
            return "用户不存在";
        }

        UserLoginCtrlEntity ctrl = userEntity.getUserLoginCtrl();
        userDao.closeForbidTime(ctrl.getId());
        return userId;
    }

    Integer calculatePasswordExpiringDays(Timestamp lastPwdModifyTime, Integer passwordValidDays) {
        Timestamp cur = new Timestamp(System.currentTimeMillis());
        Long pwdUsedDays = (cur.getTime() - lastPwdModifyTime.getTime()) / (24 * 3600 * 1000);
        return passwordValidDays - pwdUsedDays.intValue();
    }

    private UserEntity mergeEntity(UserEntity entity, UserRequest userVo) {

        if (StringUtils.isNotEmpty(userVo.getPassword())) {
            entity.setPassword(userVo.getPassword());
        }

        if (StringUtils.isNotEmpty(userVo.getNickName())) {
            entity.setNickName(userVo.getNickName());
        }

        if (StringUtils.isNotEmpty(userVo.getRealName())) {
            entity.setRealName(userVo.getRealName());
        }

        if (StringUtils.isNotEmpty(userVo.getCellphoneNum())) {
            entity.setCellphoneNum(userVo.getCellphoneNum());
        }

        if (StringUtils.isNotEmpty(userVo.getTelephoneNum())) {
            entity.setTelephoneNum(userVo.getTelephoneNum());
        }

        if (StringUtils.isNotEmpty(userVo.getEmail())) {
            entity.setEmail(userVo.getEmail());
        }

        if (StringUtils.isNotEmpty(userVo.getDescription())) {
            entity.setDescription(userVo.getDescription());
        }

        return entity;
    }


    private UserEntity request2entity(UserRequest request) throws Exception {

        Integer length = SystemCache.getInstance().getInteger("password.init.length");

        UserEntity entity = new UserEntity();
        entity.setLoginName(request.getLoginName());
        entity.setNickName(request.getNickName());
        entity.setRealName(request.getRealName());
        entity.setCellphoneNum(request.getCellphoneNum());
        entity.setTelephoneNum(request.getTelephoneNum());
        entity.setEmail(request.getEmail());
        entity.setDescription(request.getDescription());

        // 计算密码摘要
        String pwd = UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
        entity.setPassword(BCryptUtil.encode(pwd));

        // 添加登录策略
        UserLoginCtrlEntity userLoginCtrl = new UserLoginCtrlEntity();
        userLoginCtrl.setSystemInitPassword(pwd);
        userLoginCtrl.setConsecutiveAuthFailuresCount(0);
        userLoginCtrl.setIsNeedToLoginAas(true);
        entity.setUserLoginCtrl(userLoginCtrl);

        if(StringUtils.isNotEmpty(request.getRoleIds())){
            // 添加角色
            Set<UserRoleEntity> roles = new HashSet<>();
            String[] arr = request.getRoleIds().split(",");
            UserRoleEntity role = null;
            for (String id : arr) {
                role = roleService.query(id);
                if (null != role) {
                    roles.add(role);
                }
            }
            entity.setRole(roles);
        }


        return entity;
    }


    // 后台数据 转换为 前台结果
    private Page<UserResponse> entity2response(Page<UserEntity> page) {
        Page<UserResponse> response = new Page<>();

        if (null == page) {
            return response;
        }

        List<UserResponse> resultList = new ArrayList<>();
        for (UserEntity entity : page.getResult()) {
            resultList.add(entity2response(entity));
        }

        response.setTotalRows(page.getTotalRows());
        response.setTotalPages(page.getTotalPages());
        response.setResult(resultList);
        response.setStartRow(page.getStartRow());
        response.setPageSize(page.getPageSize());

        return response;
    }

    private UserResponse entity2response(UserEntity entity) {
        UserResponse response = new UserResponse();
        response.setUserId(entity.getId());
        response.setLoginName(entity.getLoginName());
        response.setNickName(entity.getNickName());
        response.setRealName(entity.getRealName());
        response.setTelephoneNum(entity.getTelephoneNum());
        response.setCellphoneNum(entity.getCellphoneNum());
        response.setEmail(entity.getEmail());
        response.setDescription(entity.getDescription());
        if (null != entity.getLoginTime()) {
            response.setLoginTime(DateUtil.date2str(entity.getLoginTime(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        }

        Set<UserRoleEntity> roles = entity.getRole();
        if (null != roles) {
            StringBuffer roleName = new StringBuffer();
            int i = 0;
            for (UserRoleEntity role : roles) {
                roleName.append(role.getRoleName());
                if (i != roles.size() - 1) {
                    roleName.append(",");
                }
                i++;
            }
            response.setRoles(roleName.toString());
        }

        long forbidTime = checkForbidTime(entity.getUserLoginCtrl());
        if (forbidTime > 0) {
            // n 单位秒，格式化到 分
            forbidTime = forbidTime / (1000 * 60);

            response.setForbid(true);
            response.setForbidTime(forbidTime);
        } else {
            response.setForbid(false);
            response.setForbidTime(null);
        }

        return response;
    }
}
