package com.ky.pm.type;

/**
 * Created by yl on 2017/8/9.
 */
public enum MethodType {

    KEYWORD_ALARM(10, "字词识别告警数据推送"),
    FACE_ALARM(11, "人脸识别告警数据推送");

    private int index;
    private String description;

    MethodType(int index, String description) {
        this.index = index;
        this.description = description;
    }

    public static MethodType getByType(String type) {
        for (MethodType method : MethodType.values()) {
            if (type.equals(method.toString())) {
                return method;
            }
        }
        return null;
    }

    public static MethodType getByIndex(int index) {
        for (MethodType method : MethodType.values()) {
            if (index == method.getIndex()) {
                return method;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
