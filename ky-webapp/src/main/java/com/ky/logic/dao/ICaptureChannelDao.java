package com.ky.logic.dao;


import com.ky.logic.entity.CaptureChannelEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.CaptureChannelRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * 采集服务频道DAO接口
 * Created by yl on 2017/7/7.
 */
public interface ICaptureChannelDao {

    /**
     * 添加服务频道
     *
     * @param entity 服务频道实体
     * @throws Exception
     */
    void createCaptureChannel(CaptureChannelEntity entity) throws Exception;


    /**
     * 删除服务频道
     *
     * @param entity 服务频道
     * @throws Exception
     */
    void deleteCaptureChannel(CaptureChannelEntity entity) throws Exception;

    /**
     * 修改服务频道
     *
     * @param entity 服务频道信息
     * @throws Exception
     */
    void updateCaptureChannel(CaptureChannelEntity entity) throws Exception;

    /**
     * 查询服务频道详情
     *
     * @param channelId 服务频道Id
     * @return 详情
     * @throws Exception
     */
    CaptureChannelEntity queryCaptureChannel(String channelId) throws Exception;

    /**
     * 分页查询服务频道列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    Page<CaptureChannelEntity> queryByPage(CaptureChannelRequest request) throws Exception;

    /**
     * 查询所有服务频道
     *
     * @return 服务频道
     * @throws Exception
     */
    List<CaptureChannelEntity> queryAll() throws Exception;

    /**
     * 根据主机id 查频道列表
     *
     * @param hostId 主机id
     * @return 频道列表
     * @throws Exception
     */
    Page<CaptureChannelEntity> queryChannelsByHostId(String hostId, Paging paging) throws Exception;
}
