package com.ky.logic.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 保存自动登录
 * Remember Me 持久化保存记录
 * 指定表名 persistent_logins 不能修改
 * Created by yl on 2017/8/15.
 */
@Entity
@Table(name = "persistent_logins")
public class PersistentLoginsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 指定表列为 username 不能修改
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 指定表列为 series 不能修改
     */
    @Column(name = "series", nullable = false)
    private String series;

    /**
     * 指定表列为 token 不能修改
     */
    @Column(name = "token", nullable = false)
    private String token;

    /**
     * 指定表列为 last_used 不能修改
     */
    @Column(name = "last_used", nullable = false)
    private Date lastUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
}
