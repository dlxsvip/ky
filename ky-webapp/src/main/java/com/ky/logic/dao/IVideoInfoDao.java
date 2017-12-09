package com.ky.logic.dao;


import com.ky.logic.entity.VideoInfoEntity;
import com.ky.logic.model.Paging;

import java.util.List;

/**
 * Created by yl on 2017/7/13.
 */
public interface IVideoInfoDao {

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
     * 查询 n 天前的 视频条数
     *
     * @param n 几天前
     * @return 视频条数
     * @throws Exception
     */
    long queryCountByCreateTime(int n) throws Exception;

    /**
     * 查询n 天前的视频oss地址列表
     *
     * @param n 几天前
     * @return oss地址列表
     * @throws Exception
     */
    List<String> queryRemoteAddressByCreateTime(int n, Paging paging) throws Exception;

    /**
     * 更新 视频oss地址为空
     * @param remoteAddress 视频地址列表
     */
    void updateVideoRemoteAddress(String remoteAddress);
}
