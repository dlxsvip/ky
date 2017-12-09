package com.ky.logic.service;

import com.ky.logic.model.info.ResponseInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by yl on 2017/7/15.
 */
public interface IUploadService {


    /**
     * 上传到oss
     *
     * @param file 文件
     * @return 通用结果
     */
    ResponseInfo upload2oss(MultipartFile file);

    /**
     * 上传到本地
     *
     * @param file 文件
     * @param dir  目录
     * @return 通用结果
     */
    ResponseInfo upload2local(MultipartFile file, String dir);


    /**
     * 上传本地文件到oss
     *
     * @param filePath 本地文件
     * @return 通用结果
     */
    String upload2oss(String filePath);

    /**
     * 删除OSS上文件
     *
     * @param failPath 文件全路径
     */
    void delete4oss(String failPath);

    /**
     * 删除本地文件
     *
     * @param failPath 文件路径
     */
    void delete4local(String failPath);
}
