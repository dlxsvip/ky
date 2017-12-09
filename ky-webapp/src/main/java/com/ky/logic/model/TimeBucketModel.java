package com.ky.logic.model;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/13.
 */
public class TimeBucketModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private Integer startTime;

    private Integer endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
