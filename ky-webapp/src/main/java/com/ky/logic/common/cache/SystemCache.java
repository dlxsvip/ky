package com.ky.logic.common.cache;


import com.ky.logic.entity.ConfigEntity;
import com.ky.logic.service.ISystemConfigService;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.ReadUtil;
import com.ky.logic.utils.SpringContextUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yl on 2017/7/22.
 */
public class SystemCache {

    private static final String SYSTEM_CONFIG = "/system-config.properties";

    // 单例 实例
    private static SystemCache INSTANCE;

    // 自定义缓存map
    private static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();

    private ISystemConfigService systemConfigService;

    // 私有化构造器
    private SystemCache() {
        if (null == systemConfigService) {
            systemConfigService = (ISystemConfigService) SpringContextUtil.getBean("SystemConfigService");
        }

        init();
    }

    private void init() {
        cacheMap.clear();

        // 先读配置文件
        readProperties();

        // 再读数据库
        readDb();

        // 缓存加载时间
        cacheMap.put("cacheDate", new Date());
    }

    // 双重锁
    public static SystemCache getInstance() {
        if (null == INSTANCE) {
            synchronized (SystemCache.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SystemCache();
                }
            }
        }

        return INSTANCE;
    }


    public String getString(String key) {
        return (String) getByKey(key);
    }

    public Integer getInteger(String key) {
        return Integer.valueOf((String) getByKey(key));
    }

    public Long getLong(String key) {
        return Long.valueOf((String) getByKey(key));
    }

    public Float getFloat(String key) {
        return Float.valueOf((String) getByKey(key));
    }

    public Double getDouble(String key) {
        return Double.valueOf((String) getByKey(key));
    }

    public BigDecimal getBigDecimal(String key) {
        return new BigDecimal((String) getByKey(key));
    }

    public Boolean getBoolean(String key) {
        return Boolean.valueOf((String) getByKey(key));
    }

    public Boolean containsKey(String key) {
        return cacheMap.containsKey(key);
    }

    public boolean isEmpty(String key) {
        return null == cacheMap.get(key) || ((String) cacheMap.get(key)).trim().length() == 0;
    }

    public boolean isNotEmpty(String key) {
        return !isEmpty(key);
    }

    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    public void clear() {
        cacheMap.clear();
    }

    private Object getByKey(String key) {
        /*if(isCache()){
            init();
        }*/

        return cacheMap.get(key);
    }

    public void updateByKey(String key, String value) {
        cacheMap.put(key, value);
    }

    private static void readProperties() {
        try {
            cacheMap = ReadUtil.read2concurrentMap(SYSTEM_CONFIG, "UTF-8");
        } catch (Exception e) {
            LoggerUtil.errorSysLog(SystemCache.class.getName(), "readProperties", "读取配置文件异常：" + e.getMessage());
        }
    }

    private void readDb() {
        try {
            List<ConfigEntity> configs = systemConfigService.getConfigs();

            for (ConfigEntity config : configs) {
                cacheMap.put(config.getConfigKey(), config.getConfigValue());
            }
        } catch (Exception e) {
            LoggerUtil.errorSysLog(SystemCache.class.getName(), "readDb", "读取数据库配置异常：" + e.getMessage());
        }
    }

    // 是否刷新缓存
    private boolean isCache() {
        try {
            if (cacheMap.isEmpty()) {
                return true;
            }

            // 上次刷新缓存的时间
            Date cacheDate = (Date) cacheMap.get("cacheDate");

            if (cacheMap.containsKey("cache.time")) {
                String granularity = "0";
                String dw = "s";
                String tmp = ((String) cacheMap.get("cache.time")).trim();

                if ("0".equals(tmp)) {
                    return false;
                } else if (tmp.contains("s")) {
                    dw = "s";
                    granularity = tmp.replace("s", "");
                } else if (tmp.contains("m")) {
                    dw = "m";
                    granularity = tmp.replace("m", "");
                } else if (tmp.contains("h")) {
                    dw = "h";
                    granularity = tmp.replace("h", "");
                } else if (tmp.contains("d")) {
                    dw = "d";
                    granularity = tmp.replace("d", "");
                } else {
                    // 实时读
                    granularity = "0";
                    dw = "s";
                }

                float n = Float.parseFloat(granularity);
                return DateUtil.isTimeOut(cacheDate, n, dw);
            }
        } catch (Exception e) {
            LoggerUtil.errorSysLog(SystemCache.class.getName(), "检查是否刷新系统配置缓存异常", e.getMessage());
            // 异常，则重新读取配置文件
            return true;
        }

        return false;
    }
}
