package com.ky.logic.utils;

/**
 * 视频工具
 * Created by yl on 2017/7/26.
 */
public class VideoUtil {

    /**
     * 视频时间转换
     *
     * @param second 秒
     * @return 对应视频播放时间
     */
    public static String getTime(Integer second) {
        String HH = "00";
        String mm = "00";
        String ss = "00";

        // 进时
        int h_shang = 0;
        int h_yu = 0;
        if (second >= 3600) {
            h_shang = second / 3600;
            h_yu = second % 3600;
            HH = ten(h_shang);

            second = h_yu;
        }

        //  进分
        int m_shang = 0;
        int m_yu = 0;
        if (second >= 60) {
            m_shang = second / 60;
            m_yu = second % 60;
            mm = ten(m_shang);

            second = m_yu;
        }

        ss = ten(second);

        return HH + ":" + mm + ":" + ss;
    }

    private static String ten(Integer t) {
        if (9 < t) {
            return t + "";
        } else {
            return "0" + t;
        }
    }


}
