package com.ky.logic.common.cache;


import com.ky.logic.entity.UserEntity;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.LoggerUtil;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存当前登陆用户
 * Created by yl on 2017/8/3.
 */
public class CurLoginUserCache {

    // 当前登录用户缓存  用户登陆名:用户
    private static ConcurrentMap<String, UserEntity> loginUser = new ConcurrentHashMap<>();

    /**
     * 放入缓存
     *
     * @param userLoginName 用户登陆名
     * @param user          用户
     */
    public static void putUser(String userLoginName, UserEntity user) {

        if (isClear()) {
            // 清空缓存
            clear();
            // 重设缓存时间
            UserEntity cacheDateUser = new UserEntity();
            cacheDateUser.setCreateTime(new Date());
            loginUser.put("cacheDateUser", cacheDateUser);
        }


        if (!loginUser.containsKey(userLoginName)) {
            loginUser.put(userLoginName, user);
        }
    }

    /**
     * 取出缓存
     *
     * @param userLoginName 用户登陆名
     * @return 用户
     */
    public static UserEntity getUser(String userLoginName) {
        return loginUser.get(userLoginName);
    }

    /**
     * 移除缓存
     *
     * @param userLoginName 用户登陆名
     */
    public static void remove(String userLoginName) {
        loginUser.remove(userLoginName);
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        loginUser.clear();
    }


    // 是否刷新缓存  7 天 刷新一次
    private static boolean isClear() {
        try {
            // 上次刷新缓存的时间
            UserEntity cacheDateUser = loginUser.get("cacheDateUser");
            if (null == cacheDateUser) {
                //设置缓存时间
                return true;
            }

            // 上次缓存时间
            Date cacheDate = cacheDateUser.getCreateTime();

            // 度量
            Float n = SystemCache.getInstance().getFloat("current.loginUser.cacheTime");

            return DateUtil.isTimeOut(cacheDate, n, "h");
        } catch (Exception e) {
            LoggerUtil.errorSysLog(CurLoginUserCache.class.getName(), "检查当前登陆用户缓存是否清空异常", e.getMessage());
            // 异常当清空处理
            return true;
        }
    }
}
