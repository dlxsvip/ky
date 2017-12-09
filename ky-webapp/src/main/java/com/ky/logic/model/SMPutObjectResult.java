package com.ky.logic.model;

/**
 * 上传object操作的返回结果。
 * Created by yl on 2017/7/11.
 */
public class SMPutObjectResult {

    /**
     * Object的ETag值。
     */
    private String eTag;

    /**
     * 构造函数。
     */
    public SMPutObjectResult(){
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
