package com.ky.logic.controller;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.ky.logic.entity.ConfigEntity;
import com.ky.logic.entity.SystemConfigEntity;
import com.ky.logic.model.OssConfigModel;
import com.ky.logic.model.info.ResponseMsg;
import com.ky.logic.service.ISystemConfigService;
import com.ky.logic.type.SystemConfigType;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.PrivilegeUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 系统配置
 * Created by yl on 2017/7/7.
 */
@Controller
@RequestMapping("/systemConfig")
public class SystemConfigController {


    @Resource(name = "SystemConfigService")
    private ISystemConfigService systemConfigService;

    /**
     * 设置OSS存储服务配置
     *
     * @param endPoint     终端地址,如果为空字符串，表示清除该配置
     * @param bucketName   bucket名称,如果为空字符串，表示清除该配置
     * @param accesskeyID  AK ID,如果为空字符串，表示清除该配置
     * @param accesskeyKey AK Secret,如果为空字符串，表示清除该配置
     * @return 通用消息
     */
    @RequestMapping(value = "/setOssConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg setOssConfig(@RequestParam(required = false) String endPoint,
                                    @RequestParam(required = false) String bucketName,
                                    @RequestParam(required = false) String accesskeyID,
                                    @RequestParam(required = false) String accesskeyKey) {
        String methodName = "setOssConfig";
        ResponseMsg responseMsg = new ResponseMsg();

        try {
            if (!PrivilegeUtil.HasSystemConfig()) {
                responseMsg.createFailedResponse(null, "没有系统配置权限", "");
                return responseMsg;
            }

            // 校验
            /*boolean isValidate = checkAkForBucket(endPoint, bucketName, accesskeyID, accesskeyKey);
            if (!isValidate) {
                responseMsg.createFailedResponse(null, "设置的OSS的ID与Key不匹配", "校验失败");
                return responseMsg;
            }*/

            SystemConfigEntity systemConfigEntity = systemConfigService.getSystemConfigEntity(SystemConfigType.OSSStorage);

            JSONObject object = new JSONObject();
            object.put("endPoint", endPoint);
            object.put("bucketName", bucketName);
            object.put("accesskeyID", accesskeyID);
            object.put("accesskeyKey", accesskeyKey);


            if (null != systemConfigEntity) {
                LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "更新:" + object.toString());
                // 更新
                systemConfigEntity.setConfig(object.toString());
                systemConfigService.updateConfig(systemConfigEntity);
            } else {
                LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "新添:" + object.toString());
                // 新添
                systemConfigService.addConfig(object.toString(), SystemConfigType.OSSStorage);
            }

            responseMsg.createSuccessResponse("success");
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "设置的OSS异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "设置的OSS异常," + e.getMessage());
        }

        return responseMsg;
    }


    /**
     * 获取OSS存储服务配置
     *
     * @return OSS配置
     */
    @RequestMapping(value = "/getOSSConfig", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg getOSSConfig() {
        String methodName = "getOSSConfig";
        ResponseMsg responseMsg = new ResponseMsg();

        try {
            Map<String, String> data = new HashMap<String, String>();
            OssConfigModel ossConfigModel = systemConfigService.getConfigOss();
            if (null != ossConfigModel) {
                data.put("endPoint", ossConfigModel.getEndpoint());
                data.put("bucketName", ossConfigModel.getBucketname());
                data.put("accesskeyID", ossConfigModel.getAccesskeyID());
            } else {
                data.put("endPoint", "");
                data.put("bucketName", "");
                data.put("accesskeyID", "");
            }
            responseMsg.createSuccessResponse(data);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "获取OSS异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "获取OSS异常," + e.getMessage());
        }

        return responseMsg;
    }


    /**
     * 设置OSS存储服务配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/cleanOssConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg cleanOssConfig(@RequestParam(required = false) String endPoint) {
        String methodName = "cleanOssConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            if (!PrivilegeUtil.HasSystemConfig()) {
                responseMsg.createFailedResponse(null, "没有系统配置权限", "");
                return responseMsg;
            }

            systemConfigService.cleanConfig(SystemConfigType.OSSStorage);
            responseMsg.createSuccessResponse("success");
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "清除OSS异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "清除OSS异常," + e.getMessage());
        }

        return responseMsg;
    }


    /**
     * 修改系统配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg updateConfig(@RequestBody(required = false) ConfigEntity config) {
        String methodName = "updateConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            if (!PrivilegeUtil.HasSystemConfig()) {
                responseMsg.createFailedResponse(null, "没有系统配置权限", "");
                return responseMsg;
            }

            String configId = systemConfigService.updateConfig(config);
            responseMsg.createSuccessResponse(configId);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "修改系统配置异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "修改系统配置异常," + e.getMessage());
        }

        return responseMsg;
    }

    /**
     * 批量修改系统配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/updateAllConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg updateAllConfig(@RequestBody(required = false) List<ConfigEntity> configs) {
        String methodName = "updateAllConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            if (!PrivilegeUtil.HasSystemConfig()) {
                responseMsg.createFailedResponse(null, "没有系统配置权限", "");
                return responseMsg;
            }

            if (null == configs || configs.size() == 0) {
                responseMsg.createFailedResponse(null, "至少选择修改一项", "");
                return responseMsg;
            }

            String configIds = systemConfigService.updateAllConfig(configs);

            responseMsg.createSuccessResponse(configIds);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "修改系统配置异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "修改系统配置异常," + e.getMessage());
        }

        return responseMsg;
    }

    /**
     * 查询系统配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/getAllConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg getAllConfig() {
        String methodName = "getAllConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            List<ConfigEntity> configs = systemConfigService.getConfigs();
            responseMsg.createSuccessResponse(configs);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "获取系统配置异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "获取系统配置异常," + e.getMessage());
        }

        return responseMsg;
    }


    /**
     * 添加or修改邮件通知配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/setMailConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg setMailConfig(@RequestBody(required = false) String emailConfigJson) {
        String methodName = "setMailConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            if (!PrivilegeUtil.HasSystemConfig()) {
                responseMsg.createFailedResponse(null, "没有系统配置权限", "");
                return responseMsg;
            }

            SystemConfigEntity systemConfigEntity = systemConfigService.getSystemConfigEntity(SystemConfigType.EMAIL);
            if (null != systemConfigEntity){
                LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "更新:" + emailConfigJson);
                // 更新
                systemConfigEntity.setConfig(emailConfigJson);
                systemConfigService.updateConfig(systemConfigEntity);
            }else{
                LoggerUtil.debugSysLog(this.getClass().getName(), methodName, "新添:" + emailConfigJson);
                // 新添
                systemConfigService.addConfig(emailConfigJson, SystemConfigType.EMAIL);
            }

            responseMsg.createSuccessResponse("");
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "修改邮件通知配置异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "修改邮件通知配置异常," + e.getMessage());
        }

        return responseMsg;
    }

    /**
     * 查询邮件通知配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/getMailConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg getMailConfig() {
        String methodName = "getMailConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            Map<String, Object> data = new HashMap<>();

            SystemConfigEntity email = systemConfigService.getSystemConfigEntity(SystemConfigType.EMAIL);
            if (null == email || null == email.getConfig()) {
                data.put("nickName", "");
                data.put("smtpAddress", "");
                data.put("smtpPort", null);
                data.put("mailAddress", "");
                data.put("mailPassword", "");

                responseMsg.createSuccessResponse(data);
                return responseMsg;
            }

            String config = email.getConfig();
            JSONObject object = new JSONObject(config);
            data.put("nickName", object.getString("nickName"));
            data.put("smtpAddress", object.getString("smtpAddress"));
            data.put("smtpPort", object.getInt("smtpPort"));
            data.put("mailAddress", object.getString("mailAddress"));
            data.put("mailPassword", "******");


            responseMsg.createSuccessResponse(data);
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "获取邮件通知配置异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "获取邮件通知配置异常," + e.getMessage());
        }

        return responseMsg;
    }

    /**
     * 清除邮件通知配置
     *
     * @return 通用消息
     */
    @RequestMapping(value = "/clearMailConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg clearMailConfig() {
        String methodName = "clearMailConfig";
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            if (!PrivilegeUtil.HasSystemConfig()) {
                responseMsg.createFailedResponse(null, "没有系统配置权限", "");
                return responseMsg;
            }

            systemConfigService.cleanConfig(SystemConfigType.EMAIL);
            responseMsg.createSuccessResponse("success");
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "清除邮件通知配置异常", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), methodName, "清除邮件通知配置异常," + e.getMessage());
        }

        return responseMsg;
    }

    private boolean checkAkForBucket(String endPoint, String bucketName, String accesskeyID, String accesskeyKey) {

        boolean isValidate = true;
        try {
            ClientConfiguration conf = new ClientConfiguration();
            conf.setSupportCname(true);
            conf.setSLDEnabled(true);
            OSSClient ossClient = new OSSClient(endPoint, accesskeyID, accesskeyKey, conf);
            String content = "Hello OSS";

            String key = UUID.randomUUID().toString();
            ossClient.putObject(bucketName, key, new ByteArrayInputStream(content.getBytes()));
            ossClient.deleteObject(bucketName, key);
            ossClient.shutdown();

        } catch (Exception e) {
            isValidate = false;
        }

        return isValidate;
    }


}
