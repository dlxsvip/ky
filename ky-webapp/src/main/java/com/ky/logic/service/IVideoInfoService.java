package com.ky.logic.service;


import com.ky.logic.entity.VideoInfoEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/13.
 */
public interface IVideoInfoService {

    VideoInfoEntity query(String videoId) throws Exception;

    /**
     * 查询n 天前的视频信息
     *
     * @param n 几天前
     * @return 视频列表
     * @throws Exception
     */
    List<VideoInfoEntity> queryBeforeCreateTime(int n, Paging paging) throws Exception;

    /**
     * 查询n 天前的视频OSS地址
     *
     * @param n 几天前
     * @return OSS地址列表
     * @throws Exception
     */
    Page<String> queryRemoteAddressByCreateTime(int n, Paging paging) throws Exception;

    /**
     * 更新 视频oss地址为空
     * @param remoteAddress 视频地址列表
     */
    void updateVideoRemoteAddress(List<String> remoteAddress);
}
