package com.ky.pm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yl on 2017/7/22.
 */
public class KeywordAlarmMsgModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分析结果id
     */
    private String analysisId;

    /**
     *
     */
    private String keywordId;

    private String keywordName;

    private Integer startTime;

    private Integer endTime;

    private Date createTime;

    private String text;

    private String videoId;


    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
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

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
