package com.ky.sdk.http.client;


import com.ky.sdk.http.service.LiveBroadcastService;
import com.ky.pm.model.FaceMsgModel;
import com.ky.pm.model.WordMsgModel;
import com.ky.pm.model.ResponseMsg;

/**
 * Created by yl on 2017/7/20.
 */
public class ManageClient {

    private String url;
    private String accessKeyId;
    private String accessKeySecret;
    private static final String signatureMethod = "HMAC-SHA256";
    private static final String version = "20160701";
    private static final String api = "/api";

    private LiveBroadcastService liveBroadcastService = new LiveBroadcastService(this);


    public ManageClient(String url, String accessKeyId, String accessKeySecret) {
        this.url = url;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * 关键词识别实时推送
     *
     * @param keyWordAlarm 告警信息
     * @return responseInfo
     */
    public ResponseMsg keywordAlarmSend(WordMsgModel keyWordAlarm) {
        return liveBroadcastService.keywordAlarmSend(keyWordAlarm);
    }


    /**
     * 关键人识别实时推送
     *
     * @param faceAlarm 告警信息
     * @return responseInfo
     */
    public ResponseMsg faceAlarmSend(FaceMsgModel faceAlarm) {
        return liveBroadcastService.faceAlarmSend(faceAlarm);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getApi() {
        return api;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public String getVersion() {
        return version;
    }
}
