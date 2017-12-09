package com.ky.logic.entity;


import com.ky.logic.type.CaptureStatusType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiao on 2017/6/26.
 */
@Entity(name = "video_info")
public class VideoInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "capture_host_id")
    private String captureHostId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "capture_status_type")
    @Enumerated(EnumType.STRING)
    private CaptureStatusType captureStatusType;

    /**
     * ��Ƶ�ļ��������ص�ַ���ɼ��и�OSS�ϴ�ʱ�ṩ�ĵ�ַ
     */
    @Column(name = "local_address")
    private String localAddress;

    /**
     * ��Ƶ�ļ���Զ��OSS�ĵ�ַ
     */
    @Column(name = "remote_address")
    private String remoteAddress;

    @Column(name = "object")
    private String object;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getCaptureHostId() {
        return captureHostId;
    }

    public void setCaptureHostId(String captureHostId) {
        this.captureHostId = captureHostId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CaptureStatusType getCaptureStatusType() {
        return captureStatusType;
    }

    public void setCaptureStatusType(CaptureStatusType captureStatusType) {
        this.captureStatusType = captureStatusType;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
