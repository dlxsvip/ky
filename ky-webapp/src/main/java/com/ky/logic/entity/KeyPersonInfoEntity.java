package com.ky.logic.entity;


import com.ky.logic.type.SexType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiao on 2017/6/26.
 * 关键人
 */
@Entity(name = "key_person_info")
public class KeyPersonInfoEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "person_image_address")
    private String personImageAddress;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "description")
    private String description;

    /**
     * 标签
     */
    @Column(columnDefinition="TEXT")
    private String label;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 性别
     */
    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private SexType sex;

    /**
     * 国籍
     */
    @Column(name = "nationality")
    private String nationality;

    /**
     * 地区
     */
    @Column(name = "area")
    private String area;

    /**
     * 职位
     */
    @Column(name = "rank")
    private String rank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
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

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
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
