package com.ky.logic.dao;


import com.ky.logic.entity.KeyPersonInfoEntity;
import com.ky.logic.model.request.KeyPersonInfoRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/7.
 */
public interface IKeyPersonInfoDao {

    /**
     * 添加关键人
     *
     * @param entity 关键人实体
     * @throws Exception
     */
    void createKeyPersonInfo(KeyPersonInfoEntity entity) throws Exception;


    /**
     * 删除关键人
     *
     * @param entity 关键人
     * @throws Exception
     */
    void deleteKeyPersonInfo(KeyPersonInfoEntity entity) throws Exception;

    /**
     * 修改关键人
     *
     * @param entity 关键人信息
     * @throws Exception
     */
    void updateKeyPersonInfo(KeyPersonInfoEntity entity) throws Exception;

    /**
     * 查询关键人详情
     *
     * @param personId 关键人Id
     * @return 详情
     * @throws Exception
     */
    KeyPersonInfoEntity queryKeyPersonInfo(String personId) throws Exception;

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
    Page<KeyPersonInfoEntity> queryByPage(KeyPersonInfoRequest request) throws Exception;

    /**
     * 查询所有关键人
     *
     * @return 关键人
     * @throws Exception
     */
    List<KeyPersonInfoEntity> queryAll() throws Exception;
}
