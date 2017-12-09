package com.ky.logic.service;

import com.ky.logic.entity.KeyPersonInfoEntity;
import com.ky.logic.model.request.KeyPersonInfoRequest;
import com.ky.logic.model.response.KeyPersonInfoResponse;
import com.ky.logic.utils.page.Page;

/**
 * Created by yl on 2017/7/7.
 */
public interface IKeyPersonInfoService {

    /**
     * 添加关键人
     *
     * @param request 关键人参数
     * @return 关键人Id
     * @throws Exception
     */
    String create(KeyPersonInfoRequest request) throws Exception;

    /**
     * 删除关键人
     *
     * @param personId 关键人Id
     * @return 删除改的关键人Id
     * @throws Exception
     */
    String deleteKeyPersonInfoById(String personId) throws Exception;

    /**
     * 修改关键人
     *
     * @param request 关键人信息
     * @return 被修改的关键人Id
     * @throws Exception
     */
    String updateKeyPersonInfo(KeyPersonInfoRequest request) throws Exception;

    /**
     * 查询关键人详情
     *
     * @param personId 关键人Id
     * @return 详情
     * @throws Exception
     */
    KeyPersonInfoResponse query(String personId) throws Exception;

    /**
     * 根据名字查询
     *
     * @param personName 人名
     * @return 结果
     * @throws Exception
     */
    KeyPersonInfoEntity queryByName(String personName) throws Exception;

    /**
     * 分页查询关键人列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    Page<KeyPersonInfoResponse> queryByPage(KeyPersonInfoRequest request) throws Exception;

}
