package com.ky.logic.type;

/**
 * Created by yl on 2017/8/2.
 */
public enum PrivilegeType {
    ALL(0, "全部"),
    USER_CONFIG(1, "用户管理"),
    SYSTEM_CONFIG(2, "系统配置"),
    MSG_REGISTER(3, "信息注册"),
    VIDEO_RESULT(4, "视频查看");

    private int index;
    private String type;

    PrivilegeType(int index, String type) {
        this.index = index;
        this.type = type;
    }
}
