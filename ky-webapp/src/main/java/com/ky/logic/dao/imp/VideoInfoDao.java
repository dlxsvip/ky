package com.ky.logic.dao.imp;

import com.ky.logic.dao.IVideoInfoDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.VideoInfoEntity;
import com.ky.logic.model.Paging;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by yl on 2017/7/13.
 */
@Repository("videoInfoDao")
public class VideoInfoDao extends AbstractHibernateDao<VideoInfoEntity> implements IVideoInfoDao {

    @Override
    public VideoInfoEntity query(String videoId) throws Exception {
        Session session = getCurrentSession();
        VideoInfoEntity entity = null;
        Criteria resultCr = session.createCriteria(VideoInfoEntity.class)
                .add(Restrictions.eq("id", videoId));

        List<VideoInfoEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    /**
     * 查询n 天前的视频信息
     *
     * @param n 几天前
     * @return 视频列表
     * @throws Exception
     */
    @Override
    public List<VideoInfoEntity> queryBeforeCreateTime(int n, Paging paging) throws Exception {
        /*
        -- 10 天前的数据
        SELECT *  FROM video_info WHERE DATEDIFF(create_time,NOW()) < -10;

        -- 10天前 -- 20 天前内的数据
        SELECT *  FROM video_info WHERE DATEDIFF(create_time,NOW()) < -10 AND DATEDIFF(create_time,NOW())>-20;

        -- 20 天前的数据
        SELECT *  FROM video_info WHERE DATEDIFF(create_time,NOW()) < -20;

        */

        Session session = getCurrentSession();
        StringBuffer sql = new StringBuffer();
        // 查询 n 天前的数据
        sql.append("SELECT *  FROM video_info WHERE DATEDIFF(create_time,NOW()) < ").append(-n);

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(VideoInfoEntity.class));
        query.setFirstResult(paging.getPageSize() * (paging.getPageNum() - 1));
        query.setMaxResults(paging.getPageSize());

        return query.list();
    }

    /**
     * 查询 n 天前的 视频条数
     *
     * @param n 几天前
     * @return 视频条数
     * @throws Exception
     */
    @Override
    public long queryCountByCreateTime(int n) throws Exception {
        Session session = getCurrentSession();
        StringBuffer sql = new StringBuffer();
        // 查询 n 天前的数据
        sql.append("SELECT count(*)  FROM video_info WHERE DATEDIFF(create_time,NOW()) < ").append(-n)
                .append(" AND !ISNULL(remote_address) AND remote_address != '' ");

        Query query = session.createSQLQuery(sql.toString());

        List<BigInteger> list = query.list();
        if (null == list || list.isEmpty()) {
            return 0L;
        }

        BigInteger b = list.get(0);
        return b.longValue();
    }

    /**
     * 查询n 天前的视频oss地址列表
     *
     * @param n      几天前
     * @param paging
     * @return oss地址列表
     * @throws Exception
     */
    @Override
    public List<String> queryRemoteAddressByCreateTime(int n, Paging paging) throws Exception {
        Session session = getCurrentSession();
        StringBuffer sql = new StringBuffer();
        // 查询 n 天前的数据
        sql.append("SELECT remote_address  FROM video_info WHERE DATEDIFF(create_time,NOW()) < ").append(-n)
                .append(" AND !ISNULL(remote_address) AND remote_address != '' ");

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());
        query.setFirstResult(paging.getPageSize() * (paging.getPageNum() - 1));
        query.setMaxResults(paging.getPageSize());

        return query.list();
    }

    /**
     * 更新 视频oss地址为空
     *
     * @param remoteAddress 视频地址列表
     */
    @Override
    public void updateVideoRemoteAddress(String remoteAddress) {
        Session session = getCurrentSession();
        StringBuffer sql = new StringBuffer();
        // 查询 n 天前的数据
        sql.append("UPDATE video_info set remote_address = NULL WHERE remote_address in (").append(remoteAddress).append(")");

        // 预编译 sql
        Query query = session.createSQLQuery(sql.toString());

    }
}
