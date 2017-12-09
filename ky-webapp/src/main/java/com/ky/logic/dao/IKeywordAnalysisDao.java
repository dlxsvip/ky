package com.ky.logic.dao;


import com.ky.logic.entity.AudioAnalysisResultEntity;
import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.model.KeywordAnalysisListModel;
import com.ky.logic.model.TimeBucketModel;
import com.ky.logic.model.request.KeyAnalysisResultRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/12.
 */
public interface IKeywordAnalysisDao {

    Page<KeywordAnalysisListModel> queryByPage(KeyAnalysisResultRequest request) throws Exception;


    /**
     * 查询分析结果概览条数
     *
     * @return 条数
     * @throws Exception
     */
    Long queryAnalysisCount(KeyAnalysisResultRequest request) throws Exception;

    /**
     * 查询分析结果概览列表
     *
     * @param request 请求参数
     * @return 列表
     * @throws Exception
     */
    List<KeywordAnalysisListModel> queryAnalysisList(KeyAnalysisResultRequest request) throws Exception;


    /**
     * 查询关键词的 时间段
     *
     * @param keywordId   关键词id
     * @param videoInfoId 视频id
     * @return 时间段
     */
    List<TimeBucketModel> queryTimeBucket(String keywordId, String videoInfoId) throws Exception;


    /**
     * 根据时间段 查询文本
     *
     * @param videoInfoId 视频id
     * @param startTime   视频id
     * @param endTime     视频id
     * @return 时间段
     * @throws Exception
     */
    AudioAnalysisResultEntity queryTimeBucketText(String videoInfoId, Integer startTime, Integer endTime) throws Exception;

    AudioAnalysisResultEntity queryTimeBucketText(String audioAnalysisId) throws Exception;


    List<AnalysisInfoModel>  queryAnalysisInfo(String analysisId) throws Exception;

    /**
     * 关键词视频结果
     *
     * @param videoId 视频id
     * @return
     */
    AnalysisInfoModel keywordAlarmResult(String videoId) throws Exception;

    List<KeywordAnalysisListModel> queryByAnalysisIds(String analysisIds) throws Exception;
}
