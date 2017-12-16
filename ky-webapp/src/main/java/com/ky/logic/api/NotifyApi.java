package com.ky.logic.api;


import com.ky.logic.common.job.accept.AcceptFaceAlarmJob;
import com.ky.logic.common.job.accept.AcceptKeywordAlarmJob;
import com.ky.logic.common.pool.AcceptPool;
import com.ky.logic.utils.JacksonUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.pm.model.FaceAlarmMsgModel;
import com.ky.pm.model.KeywordAlarmMsgModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 采集服务频道通知接口
 * Created by yl on 2017/7/7.
 */
@Controller
@RequestMapping("/channelNotify")
public class NotifyApi {



    /**
     * 接收并推送到前台
     *
     * @param keyWordAlarm 关键词识别数据
     * @return 是否成功
     */
    @RequestMapping(value = "/keyWordAlarm", method = RequestMethod.POST)
    @ResponseBody
    public String keyWordAlarm(@RequestBody KeywordAlarmMsgModel keyWordAlarm) {
        LoggerUtil.debugSysLog(this.getClass().getName(), "keyWordAlarm", "---------接收关键词识别数据成功--------");

        // 放入接收缓冲池 待处理
        AcceptPool.INSTANCE.addJob(new AcceptKeywordAlarmJob(keyWordAlarm));

        return "true";
    }

    /**
     * 接收并推送到前台
     *
     * @param faceAlarm 人脸识别数据
     * @return 是否成功
     */
    @RequestMapping(value = "/faceAlarm", method = RequestMethod.POST)
    @ResponseBody           // @ModelAttribute 接收 url 参数
    public String faceAlarm(@RequestBody FaceAlarmMsgModel faceAlarm) {
        LoggerUtil.debugSysLog(this.getClass().getName(), "faceAlarm", "---------接收人脸识别数据成功--------");
        LoggerUtil.debugSysLog(this.getClass().getName(), "faceAlarm", JacksonUtil.obj2JsonBySafe(faceAlarm));

        // 放入接收缓冲池 待处理
        AcceptPool.INSTANCE.addJob(new AcceptFaceAlarmJob(faceAlarm));

        return "true";
    }

}
