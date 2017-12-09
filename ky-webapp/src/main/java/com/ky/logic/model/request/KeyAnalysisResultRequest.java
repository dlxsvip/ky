package com.ky.logic.model.request;


import com.ky.logic.model.Paging;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/12.
 */
public class KeyAnalysisResultRequest extends Paging implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主机ID
     */
    private String hostId;

    /**
     * 主机
     */
    private String hostName;

    /**
     * 主机ip
     */
    private String hostIp;

    /**
     * 频道ID
     */
    private String channelId;

    /**
     * 频道编号
     */
    private Integer channelNo;

    /**
     * 频道
     */
    private String channelName;

    /**
     * 关键词ID
     */
    private String keywordId;

    /**
     * 关键词
     */
    private String keywordName;

    /**
     * 关键人id
     */
    private String personId;

    /**
     * 关键人
     */
    private String personName;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频OSS地址
     */
    private String remoteAddress;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


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

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
