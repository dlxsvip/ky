package com.ky.logic.entity;


import com.ky.logic.type.CaptureStatusType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 采集服务主机信息
 * Created by yl on 2017/7/11.
 */
@Entity(name = "capture_host")
public class CaptureHostEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @Id
    @Column(name = "id")
    private String id;


    /**
     * 节点名称
     */
    @Column(name = "host_name")
    private String hostName;

    /**
     * 节点ip
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 采集服务端口
     */
    @Column(name = "port")
    private String port;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 采集状态
     */
    @Column(name = "capture_status_type")
    @Enumerated(EnumType.STRING)
    private CaptureStatusType captureStatusType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CaptureStatusType getCaptureStatusType() {
        return captureStatusType;
    }

    public void setCaptureStatusType(CaptureStatusType captureStatusType) {
        this.captureStatusType = captureStatusType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
