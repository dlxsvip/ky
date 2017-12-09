package com.ky.logic.model;

/**
 * Created by yl on 2017/7/11.
 */
public class OssConfigModel {

    public String endpoint;
    public String bucketname;
    public String accesskeyID;
    public String accesskeyKey;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }

    public String getAccesskeyID() {
        return accesskeyID;
    }

    public void setAccesskeyID(String accesskeyID) {
        this.accesskeyID = accesskeyID;
    }

    public String getAccesskeyKey() {
        return accesskeyKey;
    }

    public void setAccesskeyKey(String accesskeyKey) {
        this.accesskeyKey = accesskeyKey;
    }
}
