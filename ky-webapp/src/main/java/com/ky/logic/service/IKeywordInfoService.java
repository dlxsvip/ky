package com.ky.logic.service;


import com.ky.logic.entity.KeywordInfoEntity;
import com.ky.logic.model.request.KeywordInfoRequest;
import com.ky.logic.model.response.KeywordInfoResponse;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/7.
 */
public interface IKeywordInfoService {

    /**
     * 添加关键词
     *
     * @param request 关键词参数
     * @return 关键词Id
     * @throws Exception
     */
    String create(KeywordInfoRequest request) throws Exception;

    /**
     * 删除关键词
     *
     * @param keywordId 关键词Id
     * @return 删除改的关键词Id
     * @throws Exception
     */
    String deleteKeywordInfoById(String keywordId) throws Exception;

    /**
     * 修改关键词
     *
     * @param request 关键词信息
     * @return 被修改的关键词Id
     * @throws Exception
     */
    String updateKeywordInfo(KeywordInfoRequest request) throws Exception;

    /**
     * 查询关键词详情
     *
     * @param keywordId 关键词Id
     * @return 详情
     * @throws Exception
     */
    KeywordInfoResponse query(String keywordId) throws Exception;

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
    Page<KeywordInfoResponse> queryByPage(KeywordInfoRequest request) throws Exception;

    /**
     * 查询所有的关键词
     *
     * @return 关键词集合
     * @throws Exception
     */
    List<KeywordInfoEntity> queryAll() throws Exception;

    /**
     * 查询文本里存在的 关键词
     *
     * @param text 文本
     * @return 关键词
     * @throws Exception
     */
    List<String> getKeywordsInText(String text) throws Exception;
}
