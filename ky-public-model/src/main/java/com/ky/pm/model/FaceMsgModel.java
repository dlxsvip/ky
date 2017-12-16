package com.ky.pm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yl on 2017/7/22.
 */
public class FaceMsgModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分析结果id
     */
    private String analysisId;

    private String personId;

    private String personName;

    private Integer startTime;

    private Integer endTime;

    private Date createTime;

    private String videoId;


    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
