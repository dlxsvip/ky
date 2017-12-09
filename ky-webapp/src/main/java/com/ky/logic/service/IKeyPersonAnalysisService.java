package com.ky.logic.service;


import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.model.KeyPersonAnalysisListModel;
import com.ky.logic.model.info.ResponseInfo;
import com.ky.logic.model.request.KeyAnalysisResultRequest;
import com.ky.logic.utils.page.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by yl on 2017/7/12.
 */
public interface IKeyPersonAnalysisService {

    /**
     * 查询分析结果概览列表
     *
     * @param request 分页参数
     * @return 分析结果概览列表
     * @throws Exception
     */
    Page<KeyPersonAnalysisListModel> queryByPage(KeyAnalysisResultRequest request) throws Exception;

    /**
     * 查询详情
     *
     * @param personId    关键人id
     * @param videoInfoId 视频id
     * @param hostId      主机id
     * @param channelId   频道id
     * @return
     * @throws Exception
     */
    ResponseInfo query(String personId, String videoInfoId, String hostId, String channelId) throws Exception;


    /**
     * 查询时间段 内的文本
     *
     * @param analysisId 人脸分析id
     * @param videoId    视频id
     * @return 文本
     * @throws Exception
     */
    String getTimeBucketText(String analysisId, String videoId) throws Exception;

    /**
     * 关键人分析结果
     *
     * @param analysisId 分析结果id
     * @return
     */
    Map<String, Object> liveBroadcastResult(String analysisId) throws Exception;

    /**
     * 关键人视频信息
     *
     * @param videoId 视频id
     * @return
     */
    AnalysisInfoModel faceAlarmResult(String videoId) throws Exception;

    List<KeyPersonAnalysisListModel> queryByAnalysisIds(String analysisIds) throws Exception;
}
