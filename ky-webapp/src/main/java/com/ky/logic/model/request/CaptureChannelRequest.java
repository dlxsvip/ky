package com.ky.logic.model.request;


import com.ky.logic.model.Paging;

import java.io.Serializable;

/**
 * 采集服务频道请求信息
 * Created by yl on 2017/7/11.
 */
public class CaptureChannelRequest extends Paging implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 频道ID
     */
    private String channelId;

    /**
     * 所属主机ID
     */
    private String hostId;

    /**
     * 频道编号
     */
    private Integer channelNo;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 描述
     */
    private String description;

    /**
     * 采集状态
     */
    private String captureStatusType;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Integer getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(Integer channelNo) {
        this.channelNo = channelNo;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaptureStatusType() {
        return captureStatusType;
    }

    public void setCaptureStatusType(String captureStatusType) {
        this.captureStatusType = captureStatusType;
    }


}
