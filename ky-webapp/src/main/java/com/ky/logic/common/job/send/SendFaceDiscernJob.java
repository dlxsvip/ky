package com.ky.logic.common.job.send;

import com.ky.logic.model.FaceDiscernModel;
import com.ky.logic.service.IKeyPersonInfoService;
import com.ky.logic.service.IUploadService;
import com.ky.logic.utils.JacksonUtil;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.SpringContextUtil;
import com.ky.logic.utils.WebPathUtil;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.FileInputStream;

/**
 * 注册人脸识别
 * Created by yl on 2017/7/17.
 */
public class SendFaceDiscernJob implements Runnable {

    private FaceDiscernModel faceDiscern;


    private IKeyPersonInfoService keyPersonInfoService;

    private IUploadService uploadService;

    public SendFaceDiscernJob(FaceDiscernModel faceDiscern) {
        this.faceDiscern = faceDiscern;
    }

    @Override
    public void run() {


        if (null == keyPersonInfoService) {
            keyPersonInfoService = (IKeyPersonInfoService) SpringContextUtil.getBean("keyPersonInfoService");
        }

        if (null == uploadService) {
            uploadService = (IUploadService) SpringContextUtil.getBean("uploadService");
        }

        execute();
    }

    private void execute() {
        String filePath = getLocalImagePath();
        byte[] imageByte = getImageByteByLocal(filePath);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("personName", faceDiscern.getPersonName());
        jsonObj.put("jpeg", Base64.encodeBase64String(imageByte));

        LoggerUtil.debugSysLog(this.getClass().getName(), "faceService", "人脸识别参数:" + JacksonUtil.obj2JsonBySafe(jsonObj));
        LoggerUtil.debugSysLog(this.getClass().getName(), "faceService", "人脸识别返回结果:" + JacksonUtil.obj2JsonBySafe(jsonObj));

        String ossUrl = uploadService.upload2oss(filePath);
        if (true) {
            // 上传到OSS
            //String ossUrl = uploadService.upload2oss(filePath);
            update("SUCCESS", ossUrl);
        } else {
            update("FAIL", "");
        }

    }


    private byte[] getImageByteByOss() {
        byte[] b = null;
        try {
            //b = getImageByteByLocal();
            /*if (null == b) {
                // 获取OSS 上的图片流
                InputStream in = new URL(faceDiscern.getOssImageAddress()).openStream();
                b =HttpUtil.INSTANCE.readByte(in, "");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    private byte[] getImageByteByLocal(String filePath) {
        try (FileInputStream in = new FileInputStream(filePath)) {
            int i = in.available(); // 得到文件大小
            byte data[] = new byte[i];
            in.read(data); // 读数据

            return data;
        } catch (Exception e) {
            return null;
        }
    }


    private String getLocalImagePath() {
        String url = faceDiscern.getLocalImageAddress();
        url = url.substring(url.lastIndexOf("view/app/") + 9, url.length());

        String path = WebPathUtil.getAppDir() + url;

        return path;
    }


    private void update(String statusType, String ossUrl) {
        try {
            //keyPersonInfoService.updateStatusType(faceDiscern.getPersonId(),statusType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
