package com.ky.logic.common.job.accept;

import com.ky.logic.common.job.send.SendFaceAlarmJob;
import com.ky.logic.common.pool.SendPool;
import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.model.response.KeyPersonInfoResponse;
import com.ky.logic.utils.JacksonUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.SpringContextUtil;
import com.ky.pm.model.FaceAlarmMsgModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2017/7/22.
 */
public class AcceptFaceAlarmJob implements Runnable {

    private FaceAlarmMsgModel faceAlarm;

    private IKeyPersonInfoService keyPersonInfoService;

    private IKeyPersonAnalysisService keyPersonAnalysisService;

    public AcceptFaceAlarmJob(FaceAlarmMsgModel faceAlarm) {
        this.faceAlarm = faceAlarm;
    }

    @Override
    public void run() {

        if (null == keyPersonInfoService) {
            keyPersonInfoService = (IKeyPersonInfoService) SpringContextUtil.getBean("keyPersonInfoService");
        }

        if (null == keyPersonAnalysisService) {
            keyPersonAnalysisService = (IKeyPersonAnalysisService) SpringContextUtil.getBean("keyPersonAnalysisService");
        }

        execute();
    }


    private void execute() {
        try {
            LoggerUtil.debugSysLog("接收缓冲池", "数据", JacksonUtil.obj2JsonBySafe(faceAlarm));


            String videoId = faceAlarm.getVideoId();
            String personId = faceAlarm.getPersonId();

            KeyPersonInfoResponse personInfo = keyPersonInfoService.query(personId);
            AnalysisInfoModel analysisInfo = keyPersonAnalysisService.faceAlarmResult(videoId);

            if (null == personInfo || null == analysisInfo) {
                LoggerUtil.errorSysLog("接收缓冲池", "查询数据为空", "视频id:" + videoId + ",关键人id:" + personId);
                return;
            }


            Map<String, Object> msg = new HashMap<>();
            msg.put("analysisId", faceAlarm.getAnalysisId());
            msg.put("type", "person");

            msg.put("time", faceAlarm.getCreateTime().getTime());
            msg.put("personId", personId);
            msg.put("name", personInfo.getPersonName());
            msg.put("personImage", personInfo.getPersonImageAddress());

            msg.put("hostId", analysisInfo.getHostId());
            msg.put("hostName", analysisInfo.getHostName());
            msg.put("channelId", analysisInfo.getChannelId());
            msg.put("channelName", analysisInfo.getChannelName());
            msg.put("videoId", videoId);
            msg.put("videoName", analysisInfo.getVideoName());
            msg.put("audioId", "");
            // 人脸识别原始数据单位 秒
            msg.put("startTime", faceAlarm.getStartTime());
            msg.put("endTime", faceAlarm.getEndTime());


            // 放入 发送缓冲池 待处理
            SendPool.INSTANCE.addJob(new SendFaceAlarmJob(msg));
        } catch (Exception e) {
            LoggerUtil.errorSysLog("接收缓冲池", "数据处理异常", e.getMessage());
        }
    }
}
