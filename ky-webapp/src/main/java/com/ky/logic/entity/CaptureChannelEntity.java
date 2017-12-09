package com.ky.logic.entity;


import com.ky.logic.type.CaptureStatusType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 采集服务频道信息
 * Created by yl on 2017/7/11.
 */
@Entity(name = "capture_channel")
public class CaptureChannelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 频道ID
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 所属主机ID
     */
    @Column(name = "host_id")
    private String hostId;

    /**
     * 频道名称
     */
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 频道编号
     */
    @Column(name = "channel_no")
    private Integer channelNo;

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

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(Integer channelNo) {
        this.channelNo = channelNo;
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
}
