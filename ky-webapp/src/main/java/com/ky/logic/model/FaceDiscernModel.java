package com.ky.logic.model;

/**
 * Created by yl on 2017/7/17.
 */
public class FaceDiscernModel {

    /**
     * 关键人id
     */
    private String personId;

    /**
     * 关键人
     */
    private String personName;

    /**
     * 关键人头像地址
     */
    private String ossImageAddress;


    /**
     * 关键人头像地址
     */
    private String localImageAddress;

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

    public String getOssImageAddress() {
        return ossImageAddress;
    }

    public void setOssImageAddress(String ossImageAddress) {
        this.ossImageAddress = ossImageAddress;
    }

    public String getLocalImageAddress() {
        return localImageAddress;
    }

    public void setLocalImageAddress(String localImageAddress) {
        this.localImageAddress = localImageAddress;
    }
}
