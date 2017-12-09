package com.ky.logic.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用户密码工具
 * Created by yl on 2017/8/5.
 */
public class BCryptUtil {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);


    /**
     * 加密密码
     *
     * @param pwd 明文密码
     * @return 加密密码
     */
    public static String encode(String pwd) {
        return bCryptPasswordEncoder.encode(pwd);
    }

    /**
     * 校验 rawPassword密码加密后  和 encodedPassword密码是否匹配
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 加密密码
     * @return 是否匹配
     */
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(encode("admin"));
    }
}
