package com.ky.sdk.udp.client;


import com.ky.pm.model.FaceAlarmMsgModel;
import com.ky.pm.model.KeywordAlarmMsgModel;
import com.ky.pm.model.ResponseInfo;
import com.ky.sdk.udp.service.LiveBroadcastUdpService;

/**
 * Created by yl on 2017/7/20.
 */
public class ManageUdpClient {

    private String ip;
    private Integer port;
    private String accessKeyId;
    private String accessKeySecret;
    private static final String signatureMethod = "HMAC-SHA256";
    private static final String version = "20160701";

    private LiveBroadcastUdpService liveBroadcastUdpService = new LiveBroadcastUdpService(this);

    public ManageUdpClient(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 关键词识别实时推送
     *
     * @param keyWordAlarm 告警信息
     * @return responseInfo
     */
    public ResponseInfo keywordAlarmSend(KeywordAlarmMsgModel keyWordAlarm) {
        return liveBroadcastUdpService.keywordAlarmSend(keyWordAlarm);
    }


    /**
     * 关键人识别实时推送
     *
     * @param faceAlarm 告警信息
     * @return responseInfo
     */
    public ResponseInfo faceAlarmSend(FaceAlarmMsgModel faceAlarm) {
        return liveBroadcastUdpService.faceAlarmSend(faceAlarm);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public String getVersion() {
        return version;
    }
}
