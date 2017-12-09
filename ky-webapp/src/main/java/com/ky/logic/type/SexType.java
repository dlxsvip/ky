package com.ky.logic.type;

/**
 * Created by Tfs on 2017/7/20.
 */
public enum SexType {
    MALE(0, "男"),
    FEMALE(1, "女"),
    UNKNOWN(2, "未确认");

    private Integer index;
    private String type;

    SexType(Integer index, String type) {
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
