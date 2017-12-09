package com.ky.logic.dao;


import com.ky.logic.entity.CaptureHostEntity;
import com.ky.logic.model.request.CaptureHostRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * 采集服务主机DAO接口
 * Created by yl on 2017/7/7.
 */
public interface ICaptureHostDao {

    /**
     * 添加服务主机
     *
     * @param entity 服务主机实体
     * @throws Exception
     */
    void createCaptureHost(CaptureHostEntity entity) throws Exception;


    /**
     * 删除服务主机
     *
     * @param entity 服务主机
     * @throws Exception
     */
    void deleteCaptureHost(CaptureHostEntity entity) throws Exception;

    /**
     * 修改服务主机
     *
     * @param entity 服务主机信息
     * @throws Exception
     */
    void updateCaptureHost(CaptureHostEntity entity) throws Exception;

    /**
     * 查询服务主机详情
     *
     * @param hostId 服务主机Id
     * @return 详情
     * @throws Exception
     */
    CaptureHostEntity queryCaptureHost(String hostId) throws Exception;

    /**
     * 分页查询服务主机列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    Page<CaptureHostEntity> queryByPage(CaptureHostRequest request) throws Exception;

    /**
     * 查询所有服务主机
     *
     * @return 服务主机
     * @throws Exception
     */
    List<CaptureHostEntity> queryAll() throws Exception;
}
