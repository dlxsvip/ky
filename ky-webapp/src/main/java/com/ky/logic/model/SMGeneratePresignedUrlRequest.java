package com.ky.logic.model;


import com.ky.logic.type.SMHttpMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2017/7/11.
 */
public class SMGeneratePresignedUrlRequest {

    private SMHttpMethod method;


    private String bucketName;

    private String key;

    /**
     * Content-Type to url sign
     */
    private String contentType;

    /**
     * Content-MD5
     */
    private String contentMD5;

    private Date expiration;


    // 要重载的返回请求头。
    private SMResponseHeaderOverrides responseHeaders = new SMResponseHeaderOverrides();


    // 用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
    private Map<String, String> userMetadata = new HashMap<String, String>();

    private Map<String, String> queryParam = new HashMap<String, String>();

    /**
     * 构造函数。
     *
     * @param bucketName Bucket名称。
     * @param key        Object key。
     */
    public SMGeneratePresignedUrlRequest(String bucketName, String key) {
        this(bucketName, key, SMHttpMethod.GET);
    }

    /**
     * 构造函数。
     *
     * @param bucketName Bucket名称。
     * @param key        Object key。
     * @param method     GET
     */
    public SMGeneratePresignedUrlRequest(String bucketName, String key, SMHttpMethod method) {
        this.bucketName = bucketName;
        this.key = key;
        this.method = method;
    }

    public SMHttpMethod getMethod() {
        return method;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMethod(SMHttpMethod method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public void setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public SMResponseHeaderOverrides getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(SMResponseHeaderOverrides responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }

    public Map<String, String> getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(Map<String, String> queryParam) {
        this.queryParam = queryParam;
    }
}
