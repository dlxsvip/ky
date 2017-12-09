package com.ky.logic.model;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/21.
 */
public class AnalysisInfoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 关键词分析结果ID
     */
    private String analysisId;

    /**
     * 采集时间
     */
    private String createTime;

    /**
     * 关键词ID
     */
    private String keywordId;


    /**
     * 关键词
     */
    private String keywordName;

    /**
     * 关键人ID
     */
    private String personId;

    /**
     * 关键人
     */
    private String personName;

    /**
     * 关键人头像
     */
    private String personImage;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 音频分析结果ID
     */
    private String audioAnalysisId;

    /**
     * 音频分析结果文本
     */
    private String audioAnalysisText;


    /**
     * 视频里的文本合集
     */
    private String totalText;


    private String hostId;

    private String hostName;

    private String channelId;

    private String channelName;

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
    }

    public String getAudioAnalysisId() {
        return audioAnalysisId;
    }

    public void setAudioAnalysisId(String audioAnalysisId) {
        this.audioAnalysisId = audioAnalysisId;
    }

    public String getAudioAnalysisText() {
        return audioAnalysisText;
    }

    public void setAudioAnalysisText(String audioAnalysisText) {
        this.audioAnalysisText = audioAnalysisText;
    }

    public String getTotalText() {
        return totalText;
    }

    public void setTotalText(String totalText) {
        this.totalText = totalText;
    }

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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
