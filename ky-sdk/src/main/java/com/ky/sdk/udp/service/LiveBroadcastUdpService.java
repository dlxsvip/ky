package com.ky.sdk.udp.service;

import com.ky.pm.model.FaceAlarmMsgModel;
import com.ky.pm.model.KeywordAlarmMsgModel;
import com.ky.pm.model.ResponseInfo;
import com.ky.pm.type.MethodType;
import com.ky.sdk.udp.client.ManageUdpClient;
import com.ky.sdk.utils.JacksonUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2017/7/22.
 */
public class LiveBroadcastUdpService extends AUdpClientService {


    public LiveBroadcastUdpService(ManageUdpClient client) {
        super(client);
    }


    /**
     * 关键词识别实时推送
     *
     * @param keyWordAlarm 告警信息
     * @return responseInfo
     */
    public ResponseInfo keywordAlarmSend(KeywordAlarmMsgModel keyWordAlarm) {
        ResponseInfo responseInfo = new ResponseInfo();

        String json = JacksonUtil.INSTANCE.obj2str(keyWordAlarm);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("method", MethodType.KEYWORD_ALARM.getIndex());
        map.put("jsonData", json);
        byte[] data = buildData2Bytes(map);

        String result = doSend(data);
        if (StringUtils.equals("Send OK", result)) {
            responseInfo.createSuccessResponse(result);
        } else {
            responseInfo.createFailedResponse(null, "请求异常", "请求异常");
        }

        return responseInfo;
    }

    /**
     * 关键人识别实时推送
     *
     * @param faceAlarm 告警信息
     * @return responseInfo
     */
    public ResponseInfo faceAlarmSend(FaceAlarmMsgModel faceAlarm) {
        ResponseInfo responseInfo = new ResponseInfo();

        String json = JacksonUtil.INSTANCE.obj2str(faceAlarm);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("method", MethodType.FACE_ALARM.getIndex());
        map.put("jsonData", json);
        byte[] data = buildData2Bytes(map);


        String result = doSend(data);
        if (StringUtils.equals("Send OK", result)) {
            responseInfo.createSuccessResponse(result);
        } else {
            responseInfo.createFailedResponse(null, "请求异常", "请求异常");
        }

        return responseInfo;
    }


    private byte[] buildData2Bytes(Map<String, Object> map) {
        byte[] data = null;

        ByteArrayOutputStream baos = null;
        DataOutputStream out = null;
        try {
            baos = new ByteArrayOutputStream();
            out = new DataOutputStream(baos);

            buildOtherBytes(out, map);
            buildHeaderBytes(out, map);
            buildBodyBytes(out, map);

            out.flush();
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return data;
    }

    // 后续需要权限认证信息
    private void buildOtherBytes(DataOutputStream out, Map<String, Object> map) throws Exception {
        // 8
        //out.writeByte(((Integer) map.get("msgBodyVersion")).intValue());
    }

    // 方法
    private void buildHeaderBytes(DataOutputStream out, Map<String, Object> map) throws Exception {
        // 16
        out.writeShort(((Integer) map.get("method")).intValue());
    }



    // 内容
    private void buildBodyBytes(DataOutputStream out, Map<String, Object> map) throws Exception {
        byte[] b = ((String) map.get("jsonData")).getBytes("UTF-8");

        out.writeShort(b.length);
        out.write(b);
    }
}
