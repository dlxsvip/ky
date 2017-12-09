package com.ky.logic.model;


import com.ky.logic.type.CaptureStatusType;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/15.
 */
public class CaptureModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主机ip
     */
    private String ip;

    /**
     * 主机端口
     */
    private String port;

    /**
     * 频道ID
     */
    private String channelId;

    /**
     * 频道编号
     */
    private Integer channelNo;

    /**
     * 采集状态
     */
    private CaptureStatusType captureStatusType;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(Integer channelNo) {
        this.channelNo = channelNo;
    }

    public CaptureStatusType getCaptureStatusType() {
        return captureStatusType;
    }

    public void setCaptureStatusType(CaptureStatusType captureStatusType) {
        this.captureStatusType = captureStatusType;
    }
}
