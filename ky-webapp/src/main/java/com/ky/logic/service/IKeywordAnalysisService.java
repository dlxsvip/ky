package com.ky.logic.service;


import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.model.KeywordAnalysisListModel;
import com.ky.logic.model.info.ResponseInfo;
import com.ky.logic.model.request.KeyAnalysisResultRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by yl on 2017/7/12.
 */
public interface IKeywordAnalysisService {

    /**
     * 查询分析结果概览列表
     *
     * @param request 请求参数
     * @return 分析结果概览列表
     * @throws Exception
     */
    Page<KeywordAnalysisListModel> queryByPage(KeyAnalysisResultRequest request) throws Exception;

    /**
     * 查询详情
     *
     * @param keywordId 关键字ID
     * @param videoId   视频ID
     * @param hostId    视频ID
     * @param channelId 频道ID
     * @return 详情
     */
    ResponseInfo query(String keywordId, String videoId, String hostId, String channelId) throws Exception;

    /**
     * 查询时间段 内的文本
     *
     * @param audioAnalysisId 语音分析id
     * @return 文本
     * @throws Exception
     */
    String getTimeBucketText(String audioAnalysisId) throws Exception;


    /**
     * 关键词分析结果
     *
     * @param analysisId 分析结果id
     * @return
     */
    Map<String, Object> liveBroadcastResult(String analysisId) throws Exception;

    /**
     * 关键词视频结果
     *
     * @param videoId 视频id
     * @return
     */
    AnalysisInfoModel keywordAlarmResult(String videoId) throws Exception;

    List<KeywordAnalysisListModel> queryByAnalysisIds(String analysisIds) throws Exception;
}
