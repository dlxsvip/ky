package com.ky.logic.common.job.accept;

import com.ky.logic.common.job.send.SendKeywordAlarmJob;
import com.ky.logic.common.pool.SendPool;
import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.utils.JacksonUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.SpringContextUtil;
import com.ky.pm.model.KeywordAlarmMsgModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2017/7/22.
 */
public class AcceptKeywordAlarmJob implements Runnable{

    private KeywordAlarmMsgModel keyWordAlarm;

    private IKeywordAnalysisService keywordAnalysisService;

    public AcceptKeywordAlarmJob(KeywordAlarmMsgModel keyWordAlarm) {
        this.keyWordAlarm = keyWordAlarm;
    }


    @Override
    public void run() {

        if (null == keywordAnalysisService) {
            keywordAnalysisService = (IKeywordAnalysisService) SpringContextUtil.getBean("keywordAnalysisService");
        }

        execute();
    }


    private void execute() {
        try {
            LoggerUtil.debugSysLog("接收缓冲池", "数据", JacksonUtil.obj2JsonBySafe(keyWordAlarm));

            String videoId = keyWordAlarm.getVideoId();
            AnalysisInfoModel analysisInfo = keywordAnalysisService.keywordAlarmResult(videoId);
            if (null == analysisInfo) {
                LoggerUtil.errorSysLog("接收缓冲池", "查询数据为空", "视频id:" + videoId);
                return;
            }

            Map<String, Object> msg = new HashMap<>();
            msg.put("analysisId", keyWordAlarm.getAnalysisId());
            msg.put("type", "word");

            msg.put("time", keyWordAlarm.getCreateTime().getTime());
            msg.put("keywordId", keyWordAlarm.getKeywordId());
            msg.put("name", keyWordAlarm.getKeywordName());

            msg.put("hostId", analysisInfo.getHostId());
            msg.put("hostName", analysisInfo.getHostName());
            msg.put("channelId", analysisInfo.getChannelId());
            msg.put("channelName", analysisInfo.getChannelName());
            msg.put("videoId", videoId);
            msg.put("videoName", analysisInfo.getVideoName());
            msg.put("audioId", "");

            // 关键词识别原始数据单位 毫秒
            Integer startTime = keyWordAlarm.getStartTime();
            startTime = null == startTime ? 0 : startTime / 1000;
            msg.put("startTime", startTime);
            Integer endTime = keyWordAlarm.getEndTime();
            endTime = null == endTime ? 0 : endTime / 1000;
            msg.put("endTime", endTime);
            msg.put("text", keyWordAlarm.getText());


            // 放入 发送缓冲池 待处理
            SendPool.INSTANCE.addJob(new SendKeywordAlarmJob(msg));
        } catch (Exception e) {
            LoggerUtil.errorSysLog("接收缓冲池", "数据处理异常", e.getMessage());
        }
    }
}
