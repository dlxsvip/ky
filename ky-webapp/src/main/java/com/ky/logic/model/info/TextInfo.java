package com.ky.logic.model.info;

/**
 * Created by yl on 2017/8/29.
 */
public class TextInfo {

    private String data;  // 数据
    private int weigh;   // 数据所占宽度

    public TextInfo(String data, int weigh) {
        this.data = data;
        this.weigh = weigh;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getWeigh() {
        return weigh;
    }

    public void setWeigh(int weigh) {
        this.weigh = weigh;
    }
}
