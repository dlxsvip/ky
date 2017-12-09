package com.ky.logic.common.job;

import com.ky.logic.common.job.accept.AcceptFaceAlarmJob;
import com.ky.logic.common.job.accept.AcceptKeywordAlarmJob;
import com.ky.logic.common.pool.AcceptPool;
import com.ky.logic.utils.JacksonUtil;
import com.ky.pm.model.FaceAlarmMsgModel;
import com.ky.pm.model.KeywordAlarmMsgModel;
import com.ky.pm.type.MethodType;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by yl on 2017/8/9.
 */
public class DataProcessor implements Runnable {
    DataInputStream in;

    public DataProcessor(DataInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {

        try {

            // 后续需要读取 认证信息
            //int msgBodyVersion = in.readByte();

            int method = in.readShort();
            MethodType methodType = MethodType.getByIndex(method);

            int len = in.readShort();
            String jsonData = readFixString(in, len);

            if (MethodType.KEYWORD_ALARM.equals(methodType)) {
                KeywordAlarmMsgModel keyWordAlarm = JacksonUtil.json2bean(jsonData, KeywordAlarmMsgModel.class);

                // 放入接收缓冲池 待处理
                AcceptPool.INSTANCE.addJob(new AcceptKeywordAlarmJob(keyWordAlarm));
            } else if (MethodType.FACE_ALARM.equals(methodType)) {
                FaceAlarmMsgModel faceAlarm = JacksonUtil.json2bean(jsonData, FaceAlarmMsgModel.class);

                // 放入接收缓冲池 待处理
                AcceptPool.INSTANCE.addJob(new AcceptFaceAlarmJob(faceAlarm));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String readFixString(DataInputStream in, int len)
            throws IOException {
        String ret = "";
        byte[] bytes = new byte[len];
        int success = in.read(bytes, 0, len);
        if (success > 0) {
            ret = new String(bytes, "UTF-8").trim();
        }
        return ret;
    }
}
