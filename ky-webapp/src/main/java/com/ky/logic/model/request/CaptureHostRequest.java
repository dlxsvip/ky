package com.ky.logic.model.request;


import com.ky.logic.model.Paging;

import java.io.Serializable;

/**
 * 采集服务主机请求信息
 * Created by yl on 2017/7/11.
 */
public class CaptureHostRequest extends Paging implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主机ID
     */
    private String hostId;


    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 主机ip
     */
    private String ip;

    /**
     * 主机端口
     */
    private String port;

    /**
     * 描述
     */
    private String description;

    /**
     * 采集状态
     */
    private String captureStatusType;


    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCaptureStatusType() {
        return captureStatusType;
    }

    public void setCaptureStatusType(String captureStatusType) {
        this.captureStatusType = captureStatusType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
