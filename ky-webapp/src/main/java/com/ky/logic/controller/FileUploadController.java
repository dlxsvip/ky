package com.ky.logic.controller;

import com.ky.logic.model.info.ResponseMsg;
import com.ky.logic.service.IUploadService;
import com.ky.logic.utils.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 图片上传管理
 * Created by yl on 2017/7/11.
 */
@Controller
@RequestMapping("/file")
public class FileUploadController {

    @Resource(name = "uploadService")
    private IUploadService uploadService;

    /**
     * 上传到OSS服务器
     *
     * @param file      上传的文件
     * @param localDir  本地目录
     * @return 通用结果
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public ResponseMsg upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String localDir) {
        ResponseMsg responseMsg = new ResponseMsg();

        try {
            if (StringUtils.isNotEmpty(localDir)) {
                // 上传到本地
                responseMsg = uploadService.upload2local(file, localDir);
            }else{
                // 上传到OSS
                responseMsg = uploadService.upload2oss(file);
            }
        } catch (Exception e) {
            responseMsg.createFailedResponse(null, "文件上传失败", e.getMessage());
            LoggerUtil.errorSysLog(this.getClass().getName(), "upload", "文件上传失败：" + e.getMessage());
        }


        return responseMsg;
    }


}
