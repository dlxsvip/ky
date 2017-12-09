package com.ky.logic.model.response;

import java.io.Serializable;

/**
 * Created by yl on 2017/7/11.
 */
public class KeyPersonInfoResponse implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String personImageAddress;

    /**
     * 添加时间
     */
    private String createTime;

    /**
     * 是否启用
     */
    private Boolean available;

    /**
     * 描述
     */
    private String description;

    /**
     * 标签
     */
    private String label;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String sex;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 地区
     */
    private String area;

    /**
     * 职位
     */
    private String rank;

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

    public String getPersonImageAddress() {
        return personImageAddress;
    }

    public void setPersonImageAddress(String personImageAddress) {
        this.personImageAddress = personImageAddress;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
