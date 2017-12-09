package com.ky.logic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiao on 2017/7/4.
 * �ؼ��ʷ������
 */
@Entity(name = "keyword_analysis_result")
public class KeywordAnalysisResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "keyword_id")
    private String keywordId;

    @Column(name = "audio_analysis_result_id")
    private String audioAnalysisResultId;

    @Column(name = "video_info_id")
    private String videoInfoId;

    @Column(name = "create_time")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getAudioAnalysisResultId() {
        return audioAnalysisResultId;
    }

    public void setAudioAnalysisResultId(String audioAnalysisResultId) {
        this.audioAnalysisResultId = audioAnalysisResultId;
    }

    public String getVideoInfoId() {
        return videoInfoId;
    }

    public void setVideoInfoId(String videoInfoId) {
        this.videoInfoId = videoInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
