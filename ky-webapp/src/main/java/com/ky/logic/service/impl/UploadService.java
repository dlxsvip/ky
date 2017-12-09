package com.ky.logic.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.ky.logic.model.OssConfigModel;
import com.ky.logic.model.SMGeneratePresignedUrlRequest;
import com.ky.logic.model.SMObjectMetadata;
import com.ky.logic.model.info.ResponseInfo;
import com.ky.logic.service.ISystemConfigService;
import com.ky.logic.service.IUploadService;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.Oss;
import com.ky.logic.utils.WebPathUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yl on 2017/7/15.
 */
@Service("uploadService")
public class UploadService implements IUploadService {

    private String rootUrl;

    private String rootProject;

    @Resource(name = "SystemConfigService")
    private ISystemConfigService systemConfigService;


    @Autowired
    public void setRootUrl(@Value("${web.server.rootUrl}") String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @Autowired
    public void setRootProject(@Value("${web.server.project}") String rootProject) {
        this.rootProject = rootProject;
    }

    /**
     * 上传到oss
     *
     * @param file 文件
     * @return 通用结果
     */
    @Override
    public ResponseInfo upload2oss(MultipartFile file) {
        String methodName = "upload2oss";
        ResponseInfo responseInfo = new ResponseInfo();

        try (InputStream inputStream = file.getInputStream()) {

            OssConfigModel ossConfigModel = systemConfigService.getConfigOss();
            if (null == ossConfigModel) {
                responseInfo.createFailedResponse(null, "无系统OSS，请配置OSS", "无系统OSS，请配置OSS");
            }

            // 初始化 oss 客户端
            Oss oss = new Oss(ossConfigModel.getEndpoint(), ossConfigModel.getAccesskeyID(), ossConfigModel.getAccesskeyKey());

            String originalFilename = file.getOriginalFilename();
            String nameOnOss = UUID.randomUUID().toString() + "_" + originalFilename;

            // 文件上传
            SMObjectMetadata meta = new SMObjectMetadata();
            meta.setContentLength(inputStream.available());
            oss.putObject(ossConfigModel.getBucketname(), nameOnOss, inputStream, meta);

            // url配置请求
            SMGeneratePresignedUrlRequest urlReq = new SMGeneratePresignedUrlRequest(ossConfigModel.getBucketname(), nameOnOss);

            // 设置url有效期
            Date expiration = DateUtils.addDays(new Date(), 1);
            urlReq.setExpiration(expiration);

            // 生成一个包含签名信息并可以访问 数据 url。
            String url = oss.generatePresignedUrl(urlReq).toString();
            String ossUrl = url.substring(0, url.indexOf("?"));

            responseInfo.createSuccessResponse(ossUrl);

            LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "上传到OSS成功:" + ossUrl);
        } catch (OSSException e) {
            responseInfo.createFailedResponse(null, "Oss授权失败，请检查Oss配置", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "请检查OSS配置:" + e.getMessage());
        } catch (ClientException e) {
            responseInfo.createFailedResponse(null, "OSS客户端异常,请检查Oss配置", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "请检查OSS配置:" + e.getMessage());
        } catch (Exception e) {
            responseInfo.createFailedResponse(null, "OSS异常,请检查配置的OSS", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "请检查OSS配置:" + e.getMessage());
        }


        return responseInfo;
    }

    /**
     * 上传到本地
     *
     * @param file 文件
     * @param dir  目录
     * @return 通用结果
     */
    @Override
    public ResponseInfo upload2local(MultipartFile file, String dir) {
        String methodName = "upload2local";
        ResponseInfo responseInfo = new ResponseInfo();

        String filePath = WebPathUtil.getViewSubDir("data" + File.separator + dir);
        /* 构建文件目录 */
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "创建本地目录:" + fileDir);
        }

        String originalFilename = file.getOriginalFilename();
        String extensionName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newFileName = new Date().getTime() + "." + extensionName;


        try (FileOutputStream out = new FileOutputStream(filePath + newFileName)) {
            // 写入文件
            out.write(file.getBytes());
            out.flush();

            // 相对路径
            String imageUrl = "/" + rootProject + "/view/data/" + dir + "/" + newFileName;

            // http 全路径
            //String imageUrl = rootUrl + "/view/data/" + dir + "/" + newFileName;

            responseInfo.createSuccessResponse(imageUrl);

            LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "上传到本地成功:" + imageUrl);
        } catch (IOException e) {
            responseInfo.createFailedResponse(null, "本地保存文件异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "本地保存文件异常:" + e.getMessage());
        }

        return responseInfo;
    }

    /**
     * 上传本地文件到oss
     *
     * @param filePath 本地文件
     * @return 通用结果
     */
    @Override
    public String upload2oss(String filePath) {
        String methodName = "upload2local";
        ResponseInfo responseInfo = new ResponseInfo();
        String ossUrl = "";
        try {
            OssConfigModel ossConfigModel = systemConfigService.getConfigOss();
            if (null == ossConfigModel) {
                responseInfo.createFailedResponse(null, "无系统OSS，请配置OSS", "无系统OSS，请配置OSS");
            }

            // 初始化 oss 客户端
            Oss oss = new Oss(ossConfigModel.getEndpoint(), ossConfigModel.getAccesskeyID(), ossConfigModel.getAccesskeyKey());

            String originalFilename = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            String nameOnOss = UUID.randomUUID().toString() + "_" + originalFilename;


            PutObjectResult result = oss.putObject(ossConfigModel.getBucketname(), nameOnOss, new File(filePath));


            // url配置请求
            SMGeneratePresignedUrlRequest urlReq = new SMGeneratePresignedUrlRequest(ossConfigModel.getBucketname(), nameOnOss);

            // 设置url有效期
            Date expiration = DateUtils.addDays(new Date(), 1);
            urlReq.setExpiration(expiration);

            // 生成一个包含签名信息并可以访问 数据 url。
            String url = oss.generatePresignedUrl(urlReq).toString();
            ossUrl = url.substring(0, url.indexOf("?"));

            responseInfo.createSuccessResponse(ossUrl);

            LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "上传到OSS成功:" + ossUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ossUrl;
    }

    /**
     * 删除OSS上文件
     *
     * @param failPath 文件全路径
     */
    public void delete4oss(String failPath) {
        String methodName = "delete4oss";
        try {
            OssConfigModel ossConfigModel = systemConfigService.getConfigOss();
            Oss oss = new Oss(ossConfigModel.getEndpoint(), ossConfigModel.getAccesskeyID(), ossConfigModel.getAccesskeyKey(), true);

            if (null == oss) {
                LoggerUtil.errorSysLog(this.getClass().getName(), "deleteFileFormOss", "没有获取到系统OSS");
                return;
            }

            // 刪除
            oss.deleteObject(ossConfigModel.getBucketname(), failPath);

            LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "删除OSS上文件成功:" + failPath);
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "删除OSS上文件失败:" + e.getMessage());
        }
    }

    /**
     * 删除本地文件
     *
     * @param failPath 文件路径
     */
    public void delete4local(String failPath) {
        String methodName = "delete4local";
        try {
            File localFile = new File(failPath);
            if (localFile.exists()) {
                boolean b = localFile.delete();
                if (b) {
                    LoggerUtil.debugSysLog(this.getClass().getName(), failPath, " 本地文件删除成功");
                } else {
                    LoggerUtil.errorSysLog(this.getClass().getName(), failPath, " 本地文件删除失败");
                }

            } else {
                LoggerUtil.errorSysLog(this.getClass().getName(), failPath, " 非有效文件");
            }
        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), failPath, " 本地文件删除异常:" + e.getMessage());
        }
    }


}
