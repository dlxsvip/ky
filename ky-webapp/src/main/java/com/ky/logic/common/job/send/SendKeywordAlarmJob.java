package com.ky.logic.common.job.send;


import com.ky.logic.common.cache.AlarmPushCache;
import com.ky.logic.utils.JacksonUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.SpringContextUtil;
import com.ky.logic.websocket.WebSocketServerHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送关键词识别告警
 * Created by yl on 2017/7/22.
 */
public class SendKeywordAlarmJob implements Runnable {
    Map<String, Object> msg = new HashMap<>();

    private AlarmPushCache alarmPushCache;

    private WebSocketServerHandler webSocketServerHandler;

    public SendKeywordAlarmJob(Map<String, Object> msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        if (null == alarmPushCache) {
            alarmPushCache = (AlarmPushCache) SpringContextUtil.getBean("alarmPushCache");
        }

        if (null == webSocketServerHandler) {
            webSocketServerHandler = (WebSocketServerHandler) SpringContextUtil.getBean("webSocketServerHandler");
        }

        execute();
    }

    private void execute() {
        LoggerUtil.debugSysLog("推送缓冲池", "数据", JacksonUtil.obj2JsonBySafe(msg));

        // 放入缓存
        alarmPushCache.setAlarmCache(msg);

        // 推送
        Boolean b = webSocketServerHandler.sendMessageToClients(msg);

        LoggerUtil.debugSysLog("推送缓冲池", "推送", b.toString());
    }
}
