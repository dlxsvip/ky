package com.ky.logic.dao.imp;

import com.ky.logic.dao.ICaptureHostDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.CaptureHostEntity;
import com.ky.logic.model.request.CaptureHostRequest;
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
 * 采集服务主机DAO
 * Created by yl on 2017/7/7.
 */
@Repository("captureHostDao")
public class CaptureHostDao extends AbstractHibernateDao<CaptureHostEntity> implements ICaptureHostDao {

    /**
     * 添加服务主机
     *
     * @param entity 服务主机实体
     * @throws Exception
     */
    @Override
    public void createCaptureHost(CaptureHostEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.save(entity);
    }


    /**
     * 删除服务主机
     *
     * @param entity 服务主机
     * @throws Exception
     */
    @Override
    public void deleteCaptureHost(CaptureHostEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.delete(entity);
    }

    /**
     * 修改服务主机
     *
     * @param entity 服务主机信息
     * @throws Exception
     */
    @Override
    public void updateCaptureHost(CaptureHostEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.update(entity);
    }

    /**
     * 查询服务主机详情
     *
     * @param hostId 服务主机Id
     * @return 详情
     * @throws Exception
     */
    @Override
    public CaptureHostEntity queryCaptureHost(String hostId) throws Exception {
        Session session = getCurrentSession();
        CaptureHostEntity CaptureHost = null;
        Criteria resultCr = session.createCriteria(CaptureHostEntity.class)
                .add(Restrictions.eq("id", hostId));

        List<CaptureHostEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            CaptureHost = list.get(0);
        }

        return CaptureHost;

    }

    /**
     * 分页查询服务主机列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    @Override
    public Page<CaptureHostEntity> queryByPage(CaptureHostRequest request) throws Exception {
        Page<CaptureHostEntity> page = new Page<>();

        Session session = getCurrentSession();
        // 查询结果
        Criteria resultCr = session.createCriteria(CaptureHostEntity.class);

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
        List<CaptureHostEntity> list = resultCr.list();
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
     * 查询所有服务主机
     *
     * @return 服务主机
     * @throws Exception
     */
    @Override
    public List<CaptureHostEntity> queryAll() throws Exception {

        return null;
    }

    // 查询过滤条件
    private Criteria queryParam(Criteria criteria, CaptureHostRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getHostId())) {
                criteria = criteria.add(Restrictions.eq("id", request.getHostId()));
            }

            if (StringUtils.isNotEmpty(request.getHostName())) {
                criteria = criteria.add(Restrictions.like("hostName", request.getHostName(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getDescription())) {
                criteria = criteria.add(Restrictions.like("description", request.getDescription(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getIp())) {
                criteria = criteria.add(Restrictions.like("ip", request.getIp(), MatchMode.ANYWHERE));
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
                // 默认按时间 降序
                criteria.addOrder(Order.desc("ip"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return criteria;
    }
}
