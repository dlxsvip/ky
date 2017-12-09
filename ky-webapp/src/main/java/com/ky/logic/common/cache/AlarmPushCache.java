package com.ky.logic.common.cache;

import com.ky.logic.common.job.send.HistoricalDataBakJob;
import com.ky.logic.common.pool.SendPool;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.LoggerUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 推送消息缓存管理
 * Created by yl on 2017/7/21.
 */
@Component
public class AlarmPushCache {

    // 推送消息缓存 使用 LinkedHashMap 保证 输出的顺序和输入的相同
    private static Map<String, Map<String, Object>> resultCache = new LinkedHashMap<>();

    // id 缓存
    private static Map<String, Date> time = new LinkedHashMap<>();

    // 系统配置文件
    private static ConcurrentMap<String, Object> systemCache = null;

    public static void setAlarmCache(Map<String, Object> result) {
        String id = (String) result.get("analysisId");
        Integer total = SystemCache.getInstance().getInteger("live.broadcast.result");
        if (resultCache.size() >= total) {
            clean();
        }


        // 放入缓存
        resultCache.put(id, result);

        // 时间管理
        time.put(id, new Date());
    }

    public static Map<String, Map<String, Object>> getAlarmCache() {
        return resultCache;
    }


    private static void clean() {
        LinkedList<String> idList = new LinkedList<>();
        LinkedList<Map<String, Object>> infoList = new LinkedList<>();

        Set<Map.Entry<String, Date>> entities = time.entrySet();
        for (Map.Entry<String, Date> entry : entities) {
            String id = entry.getKey();
            Date time = entry.getValue();
            if (isTimeOut(time)) {

                idList.add(id);

                // 被删除的放入list
                infoList.add(resultCache.get(id));
            }
        }

        if (idList.isEmpty()) {
            return;
        }

        // 备份
        SendPool.INSTANCE.addJob(new HistoricalDataBakJob(infoList));

        // 移除
        for (String id : idList) {
            resultCache.remove(id);
            time.remove(id);
        }

    }

    private static boolean isTimeOut(Date time) {
        try {
            Float n = Float.parseFloat((String) systemCache.get("live.broadcast.result.timeout"));

            return DateUtil.isTimeOut(time, n, "h");
        } catch (Exception e) {
            LoggerUtil.errorSysLog(AlarmPushCache.class.getName(), "检查ws推送消息缓存是否超时异常", e.getMessage());
            // 异常当超时处理
            return true;
        }

    }

}
