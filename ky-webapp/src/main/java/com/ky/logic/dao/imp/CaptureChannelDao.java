package com.ky.logic.dao.imp;

import com.ky.logic.dao.ICaptureChannelDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.CaptureChannelEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.CaptureChannelRequest;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采集服务频道DAO
 * Created by yl on 2017/7/7.
 */
@Repository("captureChannelDao")
public class CaptureChannelDao extends AbstractHibernateDao<CaptureChannelEntity> implements ICaptureChannelDao {

    /**
     * 添加服务频道
     *
     * @param entity 服务频道实体
     * @throws Exception
     */
    @Override
    public void createCaptureChannel(CaptureChannelEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.save(entity);
    }


    /**
     * 删除服务频道
     *
     * @param entity 服务频道
     * @throws Exception
     */
    @Override
    public void deleteCaptureChannel(CaptureChannelEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.delete(entity);
    }

    /**
     * 修改服务频道
     *
     * @param entity 服务频道信息
     * @throws Exception
     */
    @Override
    public void updateCaptureChannel(CaptureChannelEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.update(entity);
    }

    /**
     * 查询服务频道详情
     *
     * @param channelId 服务频道Id
     * @return 详情
     * @throws Exception
     */
    @Override
    public CaptureChannelEntity queryCaptureChannel(String channelId) throws Exception {
        Session session = getCurrentSession();
        CaptureChannelEntity CaptureChannel = null;
        Criteria resultCr = session.createCriteria(CaptureChannelEntity.class)
                .add(Restrictions.eq("id", channelId));

        List<CaptureChannelEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            CaptureChannel = list.get(0);
        }

        return CaptureChannel;

    }

    /**
     * 分页查询服务频道列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    @Override
    public Page<CaptureChannelEntity> queryByPage(CaptureChannelRequest request) throws Exception {
        Page<CaptureChannelEntity> page = new Page<>();

        Session session = getCurrentSession();
        // 查询结果
        Criteria resultCr = session.createCriteria(CaptureChannelEntity.class);

        // 条件查询参数
        resultCr = queryParam(resultCr, request);

        resultCr.setProjection(Projections.rowCount());
        page.setTotalRows(Integer.valueOf(resultCr.uniqueResult().toString()));
        long totalRows = page.getTotalRows();

        // 查询记录
        resultCr.setProjection(null);

        // 分页
        if (0 != request.getPageNum() && 0 != request.getPageSize()) {
            resultCr.setFirstResult((request.getPageNum() - 1) * request.getPageSize());
        }

        if (0 != request.getPageSize()) {
            resultCr.setMaxResults(request.getPageSize());
        }

        // 查询记录
        List<CaptureChannelEntity> list = resultCr.list();
        page.setCurPage(request.getPageNum());
        page.setPageSize(request.getPageSize());
        page.setResult(list);

        // 总页数
        int totalPages;
        if (0 != request.getPageSize()) {
            if (totalRows % request.getPageSize() == 0) {
                totalPages = (int) totalRows / request.getPageSize();
            } else {
                totalPages = (int) totalRows / request.getPageSize() + 1;
            }
        } else {
            totalPages = (int) totalRows;
        }

        page.setTotalPages(totalPages);

        return page;
    }

    /**
     * 查询所有服务频道
     *
     * @return 服务频道
     * @throws Exception
     */
    @Override
    public List<CaptureChannelEntity> queryAll() throws Exception {

        return null;
    }

    /**
     * 根据主机id 查频道列表
     *
     * @param hostId 主机id
     * @return 频道列表
     * @throws Exception
     */
    @Override
    public Page<CaptureChannelEntity> queryChannelsByHostId(String hostId, Paging paging) throws Exception {
        Page<CaptureChannelEntity> page = new Page<>();

        Session session = getCurrentSession();
        // 查询结果
        Criteria resultCr = session.createCriteria(CaptureChannelEntity.class);

        // 条件查询参数
        if (StringUtils.isNotEmpty(hostId)) {
            resultCr.add(Restrictions.eq("hostId", hostId));
        }

        resultCr.setProjection(Projections.rowCount());
        page.setTotalRows(Integer.valueOf(resultCr.uniqueResult().toString()));
        long totalRows = page.getTotalRows();

        // 查询记录
        resultCr.setProjection(null);

        // 分页
        if (0 != paging.getPageNum() && 0 != paging.getPageSize()) {
            resultCr.setFirstResult((paging.getPageNum() - 1) * paging.getPageSize());
        }

        if (0 != paging.getPageSize()) {
            resultCr.setMaxResults(paging.getPageSize());
        }

        // 查询记录
        List<CaptureChannelEntity> list = resultCr.list();
        page.setCurPage(paging.getPageNum());
        page.setPageSize(paging.getPageSize());
        page.setResult(list);

        // 总页数
        int totalPages;
        if (0 != paging.getPageSize()) {
            if (totalRows % paging.getPageSize() == 0) {
                totalPages = (int) totalRows / paging.getPageSize();
            } else {
                totalPages = (int) totalRows / paging.getPageSize() + 1;
            }
        } else {
            totalPages = (int) totalRows;
        }

        page.setTotalPages(totalPages);

        return page;
    }

    // 查询过滤条件
    private Criteria queryParam(Criteria criteria, CaptureChannelRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getChannelId())) {
                criteria = criteria.add(Restrictions.eq("id", request.getChannelId()));
            }

            if (null != request.getChannelNo()) {
                criteria = criteria.add(Restrictions.eq("channelNo", request.getChannelNo()));
            }

            if (StringUtils.isNotEmpty(request.getChannelName())) {
                criteria = criteria.add(Restrictions.like("channelName", request.getChannelName(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getDescription())) {
                criteria = criteria.add(Restrictions.like("description", request.getDescription(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getCaptureStatusType())) {
                criteria = criteria.add(Restrictions.eq("captureStatusType", request.getCaptureStatusType()));
            }


            // 按字段排序
            if (StringUtils.isNotEmpty(request.getOrderBy())) {
                if (request.getOrderAsc()) {
                    criteria.addOrder(Order.asc(request.getOrderBy()));
                } else {
                    criteria.addOrder(Order.desc(request.getOrderBy()));
                }
            } else {
                //  降序
                criteria.addOrder(Order.desc("channelNo"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return criteria;
    }
}
