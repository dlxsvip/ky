package com.ky.logic.entity;


import com.ky.logic.utils.IdUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yl on 2017/7/25.
 */
@Entity
@Table(name = "user_login_ctrl")
public class UserLoginCtrlEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 密码连续输错次数
     */
    @Column(name = "consecutive_auth_failures_count")
    private Integer consecutiveAuthFailuresCount = 0;

    /**
     * 密码连续输错超过次数后的禁闭时间
     */
    @Column(name = "last_attempt_login_time")
    private Timestamp lastAttemptLoginTime;

    /**
     * 密码修改时间
     */
    @Column(name = "last_pwd_modify_time")
    private Timestamp lastPwdModifyTime;

    /**
     * 登录时跳转，是否需要修改系统初始密码
     */
    @Column(name = "is_need_to_login_aas")
    private Boolean isNeedToLoginAas = false;

    /**
     * 系统初始密码
     */
    @Column(name = "system_init_password")
    private String systemInitPassword;

    /**
     * 暂时无用
     */
    @Column(name = "authentic_secret")
    private String authenticSecret;

    @OneToOne(mappedBy = "userLoginCtrl")
    private UserEntity user;

    public UserLoginCtrlEntity() {
        this.id = IdUtil.getIdWorker().nextUUID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getConsecutiveAuthFailuresCount() {
        return consecutiveAuthFailuresCount;
    }

    public void setConsecutiveAuthFailuresCount(Integer consecutiveAuthFailuresCount) {
        this.consecutiveAuthFailuresCount = consecutiveAuthFailuresCount;
    }

    public Timestamp getLastAttemptLoginTime() {
        return lastAttemptLoginTime;
    }

    public void setLastAttemptLoginTime(Timestamp lastAttemptLoginTime) {
        this.lastAttemptLoginTime = lastAttemptLoginTime;
    }

    public Timestamp getLastPwdModifyTime() {
        return lastPwdModifyTime;
    }

    public void setLastPwdModifyTime(Timestamp lastPwdModifyTime) {
        this.lastPwdModifyTime = lastPwdModifyTime;
    }

    public Boolean getIsNeedToLoginAas() {
        return isNeedToLoginAas;
    }

    public void setIsNeedToLoginAas(Boolean isNeedToLoginAas) {
        this.isNeedToLoginAas = isNeedToLoginAas;
    }

    public String getSystemInitPassword() {
        return systemInitPassword;
    }

    public void setSystemInitPassword(String systemInitPassword) {
        this.systemInitPassword = systemInitPassword;
    }

    public String getAuthenticSecret() {
        return authenticSecret;
    }

    public void setAuthenticSecret(String authenticSecret) {
        this.authenticSecret = authenticSecret;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
