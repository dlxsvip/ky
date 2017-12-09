package com.ky.logic.dao;


import com.ky.logic.entity.AudioAnalysisResultEntity;
import com.ky.logic.entity.FaceAnalysisResultEntity;
import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.model.KeyPersonAnalysisListModel;
import com.ky.logic.model.Paging;
import com.ky.logic.model.TimeBucketModel;
import com.ky.logic.model.request.KeyAnalysisResultRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;

/**
 * Created by yl on 2017/7/12.
 */
public interface IKeyPersonAnalysisDao {

    Page<KeyPersonAnalysisListModel> queryByPage(Paging paging) throws Exception;


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
     * @param request 分页参数
     * @return 概览列表
     * @throws Exception
     */
    List<KeyPersonAnalysisListModel> queryAnalysisList(KeyAnalysisResultRequest request) throws Exception;


    /**
     * 查询关键人的 时间段
     *
     * @param personId    关键人id
     * @param videoInfoId 视频id
     * @return 时间段
     */
    List<TimeBucketModel> queryTimeBucket(String personId, String videoInfoId) throws Exception;


    /**
     * 根据时间段 查询文本
     *
     * @param videoId     视频id
     * @param startTime   视频开始时间
     * @param endTime     视频结束时间
     * @return 时间段
     * @throws Exception
     */
    AudioAnalysisResultEntity queryTimeBucketText(String videoId, Integer startTime, Integer endTime) throws Exception;

    List<AudioAnalysisResultEntity> getTimeBucketText(String videoId, Integer startTime, Integer endTime) throws Exception;

    FaceAnalysisResultEntity queryByFaceAnalysisId(String faceAnalysisId) throws Exception;

    List<AudioAnalysisResultEntity> queryByVideoId(String videoId) throws Exception;

    List<AnalysisInfoModel>  queryAnalysisInfo(String analysisId) throws Exception;


    /**
     * 关键人视频信息
     *
     * @param videoId 视频id
     * @return
     */
    AnalysisInfoModel faceAlarmResult(String videoId) throws Exception;

    List<KeyPersonAnalysisListModel> queryByAnalysisIds(String analysisIds) throws Exception;
}
