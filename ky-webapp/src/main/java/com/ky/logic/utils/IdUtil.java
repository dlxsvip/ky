package com.ky.logic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class IdUtil {

    // id 模板
    private static final String ID_TEMP = "%05d-%d-%08d";

    // 时间格式化模板
    private static final String DATE_TEMP = "yyyy-MM-dd";

    private static SimpleDateFormat sd;

    // 单例 实例
    private static IdUtil INSTANCE;

    // 首段 随机数
    private long workerId;

    // 中段 时间戳
    private long timestamp;

    // 尾段 序列号
    private long sequence = 0L;


    private final static long workerIdBits = 0xFFFF;
    private final static long sequenceBits = 0xFFFFFFFF;


    // 私有化构造器
    private IdUtil() {
    }

    /**
     * 获取单例
     *
     * @return 实例
     */
    public static synchronized IdUtil getIdWorker() {
        if (null == INSTANCE) {
            INSTANCE = new IdUtil();
        }

        if (null == sd) {
            sd = new SimpleDateFormat(DATE_TEMP);
        }

        return INSTANCE;
    }


    /**
     * 生成 id
     *
     * @return id
     */
    public synchronized String nextUUID() {
        initWorkerId();

        // 使用UUID
        return String.format("%05d-%s", this.workerId, UUID.randomUUID().toString());
    }

    /**
     * 正常的 32 uuid
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 去掉"-"符号 的uuid
     *
     * @return uuid
     */
    public static String uuidTmp() {
        String s = uuid();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }


    /**
     * 生成指定长度的 uuid
     *
     * @param length 长度
     * @return uuid
     */
    public static String uuid(Integer length) {
        if (length < 0) {
            return "";
        }
        String tmp = uuidTmp();

        return tmp.substring(0, length);
    }


    /**
     * 生成 时间戳+随机数+用户ID的 序号
     *
     * @return id
     */
    public synchronized String nextId(Long userId) {
        // 时间戳
        String dateStr = DateUtil.date2str(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS);

        // 4位随机数
        Random random = new Random();
        String fourDigitNumber = String.format("%04d", random.nextInt(9999));

        // 用户id取后四位（不足四位前面补零）
        String userNum = String.format("%04d", userId % 10000);


        return dateStr + fourDigitNumber + userNum;
    }

    public synchronized String nextTradeId() {
        initWorkerId();
        initTimestamp();
        initSequence();


        // 使用有意义的序列
        return String.format(ID_TEMP, this.workerId, this.timestamp, this.sequence);
    }

    private void initWorkerId() {
        long t = 0;
        try {
            // 按天 级别的随机数
            t = sd.parse(sd.format(timeGen())).getTime();
        } catch (ParseException e) {
            // 当前毫秒值
            t = timeGen();
        }

        this.workerId = (new Random(t).nextLong() & workerIdBits);
    }

    private void initTimestamp() {
        long timestamp = this.timeGen();
        if (this.timestamp == timestamp) {
            timestamp = tilNextMillis(timestamp);
        }

        this.timestamp = timestamp;
    }

    private void initSequence() {
        this.sequence = (this.sequence + 1) & sequenceBits;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
