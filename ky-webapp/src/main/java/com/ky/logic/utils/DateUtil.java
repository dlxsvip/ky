package com.ky.logic.utils;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    public static final String YYYY_MM = "yyyy-MM";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String HH_MM_SS = "HH:mm:ss";

    public static final String YYYY_MM_DD_HH_MM_SS = YYYY_MM_DD + " " + HH_MM_SS;

    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";


    public static String currentDate() {
        return date2str(System.currentTimeMillis(), YYYY_MM_DD);
    }

    public static String currentTime() {
        return date2str(System.currentTimeMillis(), HH_MM_SS);
    }

    public static String currentDateTime() {
        return date2str(System.currentTimeMillis(), YYYY_MM_DD_HH_MM_SS);
    }


    public static Date parse(String dateStr, String formatStr) throws Exception {

        if (StringUtils.isEmpty(dateStr)) {
            throw new Exception("dateStr is empty.");
        }

        if (StringUtils.isEmpty(formatStr)) {
            throw new Exception("formatStr is empty.");
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.parse(dateStr);
    }

    public static Date safeParse(String dateStr, String formatStr) {
        Date result = null;
        if (StringUtils.isEmpty(dateStr) || StringUtils.isEmpty(formatStr)) {
            LoggerUtil.errorSysLog(DateUtil.class.getName(), "safeParse",
                    "dateStr or formatStr is null, dataStr : " + dateStr + ", formatStr : " + formatStr);
            return result;
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
            result = formatter.parse(dateStr);
        } catch (Exception e) {
            LoggerUtil.errorSysLog(DateUtil.class.getName(), "safeParse", e.getMessage());
        }

        return result;
    }

    public static Date clearHours(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);
        try {
            date = sf.parse(sf.format(date));
        } catch (Exception e) {
            LoggerUtil.errorSysLog(DateUtil.class.getName(), "clearHours", e.getMessage());
        }
        return date;
    }

    public static java.sql.Date toSQLDate(Date date) throws Exception {
        return new java.sql.Date(date.getTime());
    }

    public static Date subMonth(Date date, int month) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, -1 * month);

        return rightNow.getTime();
    }

    /**
     * 日期格式化
     */
    public static String date2str(Object obj, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(obj);
    }


    /**
     * 给传入的日期 增加 23小时59分59秒
     *
     * @param date 传入的日期
     * @return 增加 23小时59分59秒 后的日期
     * @author y0507
     */
    public static Date addOneDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, 23);
        c.add(Calendar.MINUTE, 59);
        c.add(Calendar.SECOND, 59);

        return c.getTime();
    }

    /**
     * 给传入的日期 增加 n 小时
     *
     * @param date 传入的日期
     * @param n    n 增加小时数、-n 减少小时数
     * @return 增加 n 小时后的日期
     * @author y0507
     */
    public static Date addHour(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, n);

        return c.getTime();
    }

    /**
     * 给传入的时间字符串增加 n 天
     *
     * @param strDate 传入格式化的时间字符串
     * @param format  格式化方式
     * @param n       n 增加天数、-n 减少天数
     * @return 增加 n 天后的格式化日期
     * @author y0507
     */
    public static String addDay(String strDate, String format, int n) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            Date dd = sf.parse(strDate);

            Date newDate = addDay(dd, n);

            return sf.format(newDate);
        } catch (ParseException e) {
            return strDate;
        }
    }

    /**
     * 给传入的时间增加 n 天 并格式化返回
     *
     * @param date   传入的日期
     * @param format 格式化方式
     * @param n      n 增加天数、-n 减少天数
     * @return 增加 n 天后的格式化日期
     * @author y0507
     */
    public static String addDay(Date date, String format, int n) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date newDate = addDay(date, n);

        return sf.format(newDate);
    }

    /**
     * 给传入的日期增加 n 天
     *
     * @param date 传入的日期
     * @param n    n 增加天数、-n 减少天数
     * @return 增加 n 天后的日期
     * @author y0507
     */
    public static Date addDay(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);

        return c.getTime();
    }

    /**
     * 给传入的时间字符串增加 n 月
     *
     * @param strDate 传入格式化的时间字符串
     * @param format  格式化方式
     * @param n       n 增加月数、-n 减少月数
     * @return 增加 n 月后的格式化日期
     * @author y0507
     */
    public static String addMonth(String strDate, String format, int n) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            Date dd = sf.parse(strDate);

            Date newDate = addMonth(dd, n);

            return sf.format(newDate);
        } catch (ParseException e) {
            return strDate;
        }
    }


    /**
     * 给传入的时间字符串增加 n 月
     *
     * @param date   时间
     * @param format 格式化方式
     * @param n      n 增加月数、-n 减少月数
     * @return 增加 n 月后的格式化日期
     * @author y0507
     */
    public static String addMonth(Date date, String format, int n) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date newDate = addMonth(date, n);

        return sf.format(newDate);
    }

    /**
     * 给传入的日期 增加 n 月
     *
     * @param date 传入的日期
     * @param n    n 增加月数、-n 减少月数
     * @return 增加 n 月后的日期
     * @author y0507
     */
    public static Date addMonth(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, n);

        return c.getTime();
    }

    /**
     * 给传入的日期 增加 n 年
     *
     * @param date 传入的日期
     * @param n    n 增加年数、-n 减少年数
     * @return 增加 n 年后的日期
     */
    public static Date addYear(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, n);

        return c.getTime();
    }

    /**
     * 时间比较
     * 例: compare(d1,">",d2)
     *
     * @param d1 待比较的 时间
     * @param d2 待比较的 时间
     * @return true or false
     * @author y0507
     */
    public static boolean compare(Date d1, String str, Date d2) {
        int a = compare(d1.getTime(), d2.getTime());
        str = str.replaceAll("\\s{1,}", "");//去掉一个或多个空格

        if (">".equals(str)) {
            return 1 == a;
        } else if (">=".equals(str)) {
            return 1 == a || 0 == a;
        } else if ("=".equals(str)) {
            return 0 == a;
        } else if ("<".equals(str)) {
            return -1 == a;
        } else if ("<=".equals(str)) {
            return -1 == a || 0 == a;
        }


        return false;
    }


    public static int compare(long t1, long t2) {
        if (t1 > t2) {
            return 1;
        } else if (t1 < t2) {
            return -1;
        } else {
            return 0;
        }
    }


    /**
     * 获取 月 1 号
     * <p/>
     * n=0: 当 月1号
     * n>0: 后n月1号
     * n<0: 前n月1号
     *
     * @param n 数字
     * @return 前|后 n 月1号
     * @author y0507
     */
    public static Date oneOfMonthDay(int n) {
        Calendar c = Calendar.getInstance();
        // 设置当月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        // 增加n月
        c.add(Calendar.MONTH, n);
        return c.getTime();
    }


    /**
     * 获取 月底最后一天
     * <p/>
     * n=0: 当 月最后一天
     * n>0: 后n月最后一天
     * n<0: 前n月最后一天
     *
     * @param n 数字
     * @return 前|后 n 月底最后一天
     * @author y0507
     */
    public static Date endOfMonthDay(int n) {
        Calendar c = Calendar.getInstance();
        // 先 增加 n + 1 月
        c.add(Calendar.MONTH, n + 1);

        // 再 设置月最后一天
        c.set(Calendar.DAY_OF_MONTH, 0);

        return c.getTime();
    }




    /**
     * 是否刷新缓存
     *
     * @param cacheDate 缓存时间
     * @param n         度量
     * @param dw        单位：s-秒、m-分、h-时、d-天
     * @return 是否刷新缓存
     */
    public static boolean isTimeOut(Date cacheDate, float n, String dw) throws Exception {
        // 现在时间
        Date nowDate = new Date();

        // 现在时间和上次刷新缓存的时间差
        long diff = nowDate.getTime() - cacheDate.getTime();


        long l = 0;
        if ("s".equals(dw)) {
            l *= 1000;
        } else if ("m".equals(dw)) {
            l *= 1000 * 60;
        } else if ("h".equals(dw)) {
            l *= 1000 * 60 * 60;
        } else if ("d".equals(dw)) {
            l *= 1000 * 60 * 60 * 24;
        } else {
            throw new Exception("不支持此单位");
        }


        // n dw的毫秒
        BigDecimal a = new BigDecimal(l);


        // n dw
        BigDecimal b = new BigDecimal(n);
        // n 小时的毫秒
        BigDecimal c = a.multiply(b);

        // 大于n小时
        if (diff > c.longValue()) {
            // 重新读缓存
            return true;
        } else {
            return false;
        }
    }
}
