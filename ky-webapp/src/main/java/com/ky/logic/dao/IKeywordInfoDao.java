package com.ky.logic.dao;


import com.ky.logic.entity.KeywordInfoEntity;
import com.ky.logic.model.request.KeywordInfoRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/7.
 */
public interface IKeywordInfoDao {

    /**
     * 添加关键词
     *
     * @param entity 关键词实体
     * @throws Exception
     */
    void createKeywordInfo(KeywordInfoEntity entity) throws Exception;


    /**
     * 删除关键词
     *
     * @param entity 关键词
     * @throws Exception
     */
    void deleteKeywordInfo(KeywordInfoEntity entity) throws Exception;

    /**
     * 修改关键词
     *
     * @param entity 关键词信息
     * @throws Exception
     */
    void updateKeywordInfo(KeywordInfoEntity entity) throws Exception;

    /**
     * 查询关键词详情
     *
     * @param keywordId 关键词Id
     * @return 详情
     * @throws Exception
     */
    KeywordInfoEntity queryKeywordInfo(String keywordId) throws Exception;

    /**
     * 根据名字查询
     *
     * @param keywordName 关键词
     * @return 结果
     * @throws Exception
     */
    KeywordInfoEntity queryByName(String keywordName) throws Exception;

    /**
     * 分页查询关键词列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    Page<KeywordInfoEntity> queryByPage(KeywordInfoRequest request) throws Exception;

    /**
     * 查询所有关键词
     *
     * @return 关键词
     * @throws Exception
     */
    List<KeywordInfoEntity> queryAll() throws Exception;
}
