package com.ky.logic.dao.imp;

import com.ky.logic.dao.IKeywordAnalysisDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.AudioAnalysisResultEntity;
import com.ky.logic.model.AnalysisInfoModel;
import com.ky.logic.model.KeywordAnalysisListModel;
import com.ky.logic.model.TimeBucketModel;
import com.ky.logic.model.request.KeyAnalysisResultRequest;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by yl on 2017/7/12.
 */
@Repository("keywordAnalysisDao")
public class KeywordAnalysisDao extends AbstractHibernateDao<KeywordAnalysisListModel> implements IKeywordAnalysisDao {

    @Override
    public Page<KeywordAnalysisListModel> queryByPage(KeyAnalysisResultRequest request) throws Exception {
        Page<KeywordAnalysisListModel> page = new Page<>();

        //Session session = getCurrentSession();
        //StringBuffer sql = getAnalysisSql("*");


        return page;
    }

    /**
     * 查询分析结果概览条数
     *
     * @return 条数
     * @throws Exception
     */
    @Override
    public Long queryAnalysisCount(KeyAnalysisResultRequest request) throws Exception {
        Session session = getCurrentSession();
        StringBuffer sql = getAnalysisSql("count(*)");

        // 过滤参数
        sql = queryParam(sql, request);


        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());

        List<BigInteger> list = query.list();
        if (null == list || list.isEmpty()) {
            return 0L;
        }

        BigInteger b = list.get(0);
        return b.longValue();
    }

    @Override
    public List<KeywordAnalysisListModel> queryAnalysisList(KeyAnalysisResultRequest request) throws Exception {
        Session session = getCurrentSession();
        StringBuffer sql = getAnalysisSql("*");
        // 过滤参数
        sql = queryParam(sql, request);

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(KeywordAnalysisListModel.class));
        query.setFirstResult(request.getPageSize() * (request.getPageNum() - 1));
        query.setMaxResults(request.getPageSize());
        List<KeywordAnalysisListModel> list = query.list();

        return list;
    }



    /**
     * 查询关键词的 时间段
     *
     * @param keywordId   关键词id
     * @param videoInfoId 视频id
     * @return 时间段
     */
    @Override
    public List<TimeBucketModel> queryTimeBucket(String keywordId, String videoInfoId) throws Exception {

        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT aar.id as id ,start_time as startTime ,end_time as endTime from keyword_analysis_result kar ");
        sql.append("RIGHT JOIN audio_analysis_result aar ");
        sql.append("ON kar.audio_analysis_result_id = aar.id ");
        sql.append("WHERE kar.keyword_id = '");
        sql.append(keywordId);
        sql.append("' and kar.video_info_id = '");
        sql.append(videoInfoId);
        sql.append("' ORDER BY aar.create_time,aar.start_time ");

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeBucketModel.class));

        return query.list();
    }

    /**
     * 根据时间段 查询文本
     *
     * @param videoInfoId 视频id
     * @param startTime   视频id
     * @param endTime     视频id
     * @return 时间段
     * @throws Exception
     */
    @Override
    public AudioAnalysisResultEntity queryTimeBucketText(String videoInfoId, Integer startTime, Integer endTime) throws Exception {
        Session session = getCurrentSession();

        // 查询结果
        Criteria resultCr = session.createCriteria(AudioAnalysisResultEntity.class);
        resultCr.add(Restrictions.eq("videoId", videoInfoId));
        resultCr.add(Restrictions.eq("startTime", startTime));
        resultCr.add(Restrictions.eq("endTime", endTime));
        List<AudioAnalysisResultEntity> list = resultCr.list();

        if (null == list || list.isEmpty()) {
            return null;
        }


        return list.get(0);
    }

    @Override
    public AudioAnalysisResultEntity queryTimeBucketText(String audioAnalysisId) throws Exception {
        Session session = getCurrentSession();
        AudioAnalysisResultEntity entity = null;
        Criteria resultCr = session.createCriteria(AudioAnalysisResultEntity.class)
                .add(Restrictions.eq("id", audioAnalysisId));

        List<AudioAnalysisResultEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    @Override
    public List<AnalysisInfoModel> queryAnalysisInfo(String analysisId) throws Exception {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("select  keyword_analysis_result.id as analysisId ,keyword_info.id as keywordId,keyword_info.keyword_name as keywordName,");
        sql.append("date_format(video_info.create_time,'%Y-%m-%d %T') as createTime,");
        sql.append("video_info.id as videoId,video_info.video_name as videoName,");
        sql.append("audio_analysis_result.id as audioAnalysisId,audio_analysis_result.text as audioAnalysisText ");
        sql.append("from keyword_analysis_result ");
        sql.append("inner Join keyword_info on keyword_analysis_result.keyword_id = keyword_info.id ");
        sql.append("inner Join video_info on keyword_analysis_result.video_info_id = video_info.id ");
        sql.append("inner Join audio_analysis_result on audio_analysis_result.video_id = video_info.id ");
        sql.append("WHERE keyword_analysis_result.id='");
        sql.append(analysisId);
        sql.append("'");

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(AnalysisInfoModel.class));

        return query.list();
    }

    /**
     * 关键词视频结果
     *
     * @param videoId 视频id
     * @return
     */
    @Override
    public AnalysisInfoModel keywordAlarmResult(String videoId) throws Exception {
        AnalysisInfoModel analysis = null;
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("select  capture_host.id as hostId,capture_host.host_name as hostName, ");
        sql.append("capture_channel.id as channelId,capture_channel.channel_name as channelName, ");
        sql.append("video_info.video_name as videoName ");
        sql.append("from video_info ");
        sql.append("inner Join capture_channel on capture_channel.id = video_info.channel_id ");
        sql.append("inner Join capture_host on capture_host.id = video_info.capture_host_id ");
        sql.append("WHERE video_info.id='");
        sql.append(videoId);
        sql.append("'");

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(AnalysisInfoModel.class));

        List<AnalysisInfoModel> list = query.list();
        if (null != list && list.size() > 0) {
            analysis = list.get(0);
        }

        return analysis;
    }

    @Override
    public List<KeywordAnalysisListModel> queryByAnalysisIds(String analysisIds) throws Exception {
        Session session = getCurrentSession();
        StringBuffer sql = getExportAnalysisSql(analysisIds);

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(KeywordAnalysisListModel.class));
        List<KeywordAnalysisListModel> list = query.list();

        return list;
    }

    private StringBuffer getAnalysisSql(String str) {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  ");
        sql.append(str);
        sql.append(" FROM ( ");
        sql.append("select keyword_analysis_result.id as analysisId, keyword_info.id as keywordId,keyword_info.keyword_name as keywordName, ");
        sql.append("date_format(video_info.create_time,'%Y-%m-%d %T') as createTime, ");
        sql.append("capture_host.id as hostId,capture_host.host_name as hostName,capture_host.ip as hostIp, ");
        sql.append("capture_channel.id as channelId,capture_channel.channel_no as channelNo, capture_channel.channel_name as channelName, ");
        sql.append("video_info.id as videoId,video_info.video_name as videoName,video_info.remote_address as remoteAddress ");
        sql.append("from keyword_analysis_result  ");
        sql.append("inner Join keyword_info on keyword_analysis_result.keyword_id = keyword_info.id ");
        sql.append("inner Join video_info on keyword_analysis_result.video_info_id = video_info.id ");
        sql.append("inner Join capture_channel on capture_channel.id = video_info.channel_id ");
        sql.append("inner Join capture_host on capture_host.id = video_info.capture_host_id ");
        sql.append("GROUP BY video_info.id,keyword_info.id ");
        sql.append(") as temp where 1=1 ");

        return sql;
    }

    private StringBuffer getExportAnalysisSql(String analysisIds) {

        StringBuffer sql = new StringBuffer();
        sql.append("select keyword_analysis_result.id as analysisId, keyword_info.id as keywordId,keyword_info.keyword_name as keywordName, ");
        sql.append("date_format(video_info.create_time,'%Y-%m-%d %T') as createTime, ");
        sql.append("capture_host.id as hostId,capture_host.host_name as hostName,capture_host.ip as hostIp, ");
        sql.append("capture_channel.id as channelId,capture_channel.channel_no as channelNo, capture_channel.channel_name as channelName, ");
        sql.append("video_info.id as videoId,video_info.video_name as videoName,video_info.remote_address as remoteAddress ");
        sql.append("from keyword_analysis_result  ");
        sql.append("inner Join keyword_info on keyword_analysis_result.keyword_id = keyword_info.id ");
        sql.append("inner Join video_info on keyword_analysis_result.video_info_id = video_info.id ");
        sql.append("inner Join capture_channel on capture_channel.id = video_info.channel_id ");
        sql.append("inner Join capture_host on capture_host.id = video_info.capture_host_id ");
        sql.append("where keyword_analysis_result.id in (");
        sql.append(analysisIds);
        sql.append(")");

        return sql;
    }

    private StringBuffer queryParam(StringBuffer sql, KeyAnalysisResultRequest request) {

        if (StringUtils.isNotEmpty(request.getKeywordId())) {
            sql.append(" and temp.keywordId = '");
            sql.append(request.getKeywordId());
            sql.append("' ");
        }

        if (StringUtils.isNotEmpty(request.getKeywordName())) {
            sql.append(" and temp.keywordName like '%");
            sql.append(request.getKeywordName());
            sql.append("%' ");
        }

        if (StringUtils.isNotEmpty(request.getHostId())) {
            sql.append(" and temp.hostId = '");
            sql.append(request.getHostId());
            sql.append("' ");
        }

        if (StringUtils.isNotEmpty(request.getHostName())) {
            sql.append(" and temp.hostName like '%");
            sql.append(request.getHostName());
            sql.append("%' ");
        }

        if (StringUtils.isNotEmpty(request.getHostIp())) {
            sql.append(" and temp.hostIp like '%");
            sql.append(request.getHostIp());
            sql.append("%' ");
        }

        if (StringUtils.isNotEmpty(request.getChannelId())) {
            sql.append(" and temp.channelId = '");
            sql.append(request.getChannelId());
            sql.append("' ");
        }

        if (null != request.getChannelNo()) {
            sql.append(" and temp.channelNo = ");
            sql.append(request.getChannelNo());
            sql.append(" ");
        }

        if (StringUtils.isNotEmpty(request.getChannelName())) {
            sql.append(" and temp.channelName like '%");
            sql.append(request.getChannelName());
            sql.append("%' ");
        }


        if (StringUtils.isNotEmpty(request.getVideoId())) {
            sql.append(" and temp.videoId = '");
            sql.append(request.getVideoId());
            sql.append("' ");
        }

        if (StringUtils.isNotEmpty(request.getVideoName())) {
            sql.append(" and temp.videoName like '%");
            sql.append(request.getVideoName());
            sql.append("%' ");
        }

        // 默认时间降序
        sql.append(" ORDER BY createTime DESC ");

        return sql;
    }


}
