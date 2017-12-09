package com.ky.logic.service;


import com.ky.logic.model.request.CaptureHostRequest;
import com.ky.logic.model.response.CaptureHostResponse;
import com.ky.logic.utils.page.Page;

/**
 * 采集服务主机接口
 * Created by yl on 2017/7/7.
 */
public interface ICaptureHostService {

    /**
     * 添加服务主机
     *
     * @param request 服务主机参数
     * @return 服务主机Id
     * @throws Exception
     */
    String create(CaptureHostRequest request) throws Exception;

    /**
     * 删除服务主机
     *
     * @param hostId 服务主机Id
     * @return 删除的服务主机Id
     * @throws Exception
     */
    String deleteCaptureHostById(String hostId) throws Exception;

    /**
     * 修改服务主机
     *
     * @param request 服务主机信息
     * @return 被修改的服务主机Id
     * @throws Exception
     */
    String updateCaptureHost(CaptureHostRequest request) throws Exception;

    /**
     * 查询服务主机详情
     *
     * @param hostId 服务主机Id
     * @return 详情
     * @throws Exception
     */
    CaptureHostResponse query(String hostId) throws Exception;

    /**
     * 分页查询服务主机列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    Page<CaptureHostResponse> queryByPage(CaptureHostRequest request) throws Exception;

    /**
     * 服务主机开始采集
     *
     * @param hostId 服务主机Id
     * @return 服务主机Id
     * @throws Exception
     */
    String start(String hostId) throws Exception;


    /**
     * 服务主机停止采集
     *
     * @param hostId 服务主机Id
     * @return 服务主机Id
     * @throws Exception
     */
    String stop(String hostId) throws Exception;
}
