package com.ky.sdk.http.service;

import com.ky.sdk.http.client.ManageClient;
import com.ky.pm.model.FaceMsgModel;
import com.ky.pm.model.WordMsgModel;
import com.ky.pm.model.ResponseMsg;
import com.ky.sdk.utils.JacksonUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yl on 2017/7/22.
 */
public class LiveBroadcastService extends AClientService {


    public LiveBroadcastService(ManageClient client) {
        super(client);
    }



    /**
     * 关键词识别实时推送
     *
     * @param keyWordAlarm 告警信息
     * @return responseInfo
     */
    public ResponseMsg keywordAlarmSend(WordMsgModel keyWordAlarm) {
        ResponseMsg responseMsg = new ResponseMsg();

        String json = JacksonUtil.INSTANCE.obj2str(keyWordAlarm);
        String result = doPost("/channelNotify/keyWordAlarm", json);
        if (StringUtils.isEmpty(result)) {
            responseMsg.createFailedResponse(null, "请求异常", "请求异常");
        } else {
            responseMsg.createSuccessResponse(result);
        }

        return responseMsg;
    }

    /**
     * 关键人识别实时推送
     *
     * @param faceAlarm 告警信息
     * @return responseInfo
     */
    public ResponseMsg faceAlarmSend(FaceMsgModel faceAlarm) {
        ResponseMsg responseMsg = new ResponseMsg();

        String json = JacksonUtil.INSTANCE.obj2str(faceAlarm);
        String result = doPost("/channelNotify/faceAlarm", json);
        if (StringUtils.isEmpty(result)) {
            responseMsg.createFailedResponse(null, "请求异常", "请求异常");
        } else {
            responseMsg.createSuccessResponse(result);
        }

        return responseMsg;
    }


}
