package com.ky.logic.service;


import com.ky.logic.model.Paging;
import com.ky.logic.model.info.ResponseInfo;
import com.ky.logic.model.request.CaptureChannelRequest;
import com.ky.logic.model.response.CaptureChannelResponse;
import com.ky.logic.model.response.CaptureHostResponse;
import com.ky.logic.type.CaptureStatusType;
import com.ky.logic.utils.page.Page;

/**
 * 采集服务频道接口
 * Created by yl on 2017/7/7.
 */
public interface ICaptureChannelService {

    /**
     * 添加服务频道
     *
     * @param request 服务频道参数
     * @return 服务频道Id
     * @throws Exception
     */
    String create(CaptureChannelRequest request) throws Exception;

    /**
     * 删除服务频道
     *
     * @param channelId 服务频道Id
     * @return 删除的服务频道Id
     * @throws Exception
     */
    String deleteCaptureChannelById(String channelId) throws Exception;

    /**
     * 修改服务频道
     *
     * @param request 服务频道信息
     * @return 被修改的服务频道Id
     * @throws Exception
     */
    String updateCaptureChannel(CaptureChannelRequest request) throws Exception;

    void updateStatusType(String channelId, CaptureStatusType statusType) throws Exception;

    /**
     * 查询服务频道详情
     *
     * @param channelId 服务频道Id
     * @return 详情
     * @throws Exception
     */
    CaptureChannelResponse query(String channelId) throws Exception;

    /**
     * 分页查询服务频道列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    Page<CaptureChannelResponse> queryByPage(CaptureChannelRequest request) throws Exception;

    /**
     * 根据主机id 查频道列表
     *
     * @param hostId 主机id
     * @return 频道列表
     * @throws Exception
     */
    Page<CaptureChannelResponse> queryChannelsByHostId(String hostId, Paging paging) throws Exception;


    /**
     * 频道开始采集
     *
     *
     * @param host    服务主机
     * @param channel 频道
     * @return 频道Id
     * @throws Exception
     */
    ResponseInfo start(CaptureHostResponse host, CaptureChannelResponse channel);

    /**
     * 频道停止采集
     *
     * @param host    服务主机
     * @param channel 频道
     * @return 频道Id
     * @throws Exception
     */
    ResponseInfo stop(CaptureHostResponse host, CaptureChannelResponse channel);

    String getCaptureUrl();

    String getCaptureAccessKeyId();

    String getCaptureAccessKeySecret();
}
