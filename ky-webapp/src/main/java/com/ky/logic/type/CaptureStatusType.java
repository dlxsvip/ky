package com.ky.logic.type;

/**
 * Created by Tfs on 2017/7/7.
 */
public enum CaptureStatusType {
    UNCAPTURE(0, "未采集"),
    START(1, "开始采集"),
    PROCESSING(2, "采集中"),
    OVER(3, "采集完成"),
    FAILED(4, "采集失败");


    private Integer index;
    private String type;

    CaptureStatusType(Integer index, String type) {
        this.index = index;
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
