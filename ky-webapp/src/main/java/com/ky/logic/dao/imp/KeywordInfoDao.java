package com.ky.logic.dao.imp;

import com.ky.logic.dao.IKeywordInfoDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.KeywordInfoEntity;
import com.ky.logic.model.request.KeywordInfoRequest;
import com.ky.logic.utils.DateUtil;
import com.ky.logic.utils.LoggerUtil;
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
 * Created by yl on 2017/7/7.
 */
@Repository("keywordInfoDao")
public class KeywordInfoDao extends AbstractHibernateDao<KeywordInfoEntity> implements IKeywordInfoDao {

    /**
     * 添加关键词
     *
     * @param entity 关键词实体
     * @throws Exception
     */
    @Override
    public void createKeywordInfo(KeywordInfoEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.save(entity);
    }


    /**
     * 删除关键词
     *
     * @param entity 关键词
     * @throws Exception
     */
    @Override
    public void deleteKeywordInfo(KeywordInfoEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.delete(entity);
    }

    /**
     * 修改关键词
     *
     * @param entity 关键词信息
     * @throws Exception
     */
    @Override
    public void updateKeywordInfo(KeywordInfoEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.update(entity);
    }

    /**
     * 查询关键词详情
     *
     * @param keywordId 关键词Id
     * @return 详情
     * @throws Exception
     */
    @Override
    public KeywordInfoEntity queryKeywordInfo(String keywordId) throws Exception {
        Session session = getCurrentSession();
        KeywordInfoEntity keywordInfo = null;
        Criteria resultCr = session.createCriteria(KeywordInfoEntity.class)
                .add(Restrictions.eq("id", keywordId));

        List<KeywordInfoEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            keywordInfo = list.get(0);
        }

        return keywordInfo;

    }

    /**
     * 根据名字查询
     *
     * @param keywordName 关键词
     * @return 结果
     * @throws Exception
     */
    @Override
    public KeywordInfoEntity queryByName(String keywordName) throws Exception {
        Session session = getCurrentSession();
        KeywordInfoEntity keywordInfo = null;
        Criteria resultCr = session.createCriteria(KeywordInfoEntity.class)
                .add(Restrictions.eq("keywordName", keywordName));

        List<KeywordInfoEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            keywordInfo = list.get(0);
        }

        return keywordInfo;
    }

    /**
     * 分页查询关键词列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    @Override
    public Page<KeywordInfoEntity> queryByPage(KeywordInfoRequest request) throws Exception {
        Page<KeywordInfoEntity> page = new Page<>();

        Session session = getCurrentSession();
        // 查询结果
        Criteria resultCr = session.createCriteria(KeywordInfoEntity.class);

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
        List<KeywordInfoEntity> list = resultCr.list();
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
     * 查询所有关键词
     *
     * @return 关键词
     * @throws Exception
     */
    @Override
    public List<KeywordInfoEntity> queryAll() throws Exception {
        Session session = getCurrentSession();
        // 查询结果
        Criteria resultCr = session.createCriteria(KeywordInfoEntity.class);
        return resultCr.list();
    }

    // 查询过滤条件
    private Criteria queryParam(Criteria criteria, KeywordInfoRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getKeywordId())) {
                criteria = criteria.add(Restrictions.eq("id", request.getKeywordId()));
            }

            if (StringUtils.isNotEmpty(request.getKeywordName())) {
                criteria = criteria.add(Restrictions.like("keywordName", request.getKeywordName(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getDescription())) {
                criteria = criteria.add(Restrictions.like("description", request.getDescription(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getLabel())) {
                criteria = criteria.add(Restrictions.like("label", request.getLabel(), MatchMode.ANYWHERE));
            }

            if (null != request.getAvailable()) {
                criteria = criteria.add(Restrictions.eq("available", request.getAvailable()));
            }

            if (StringUtils.isNotEmpty(request.getCreateTime())) {
                criteria = criteria.add(Restrictions.eq("createTime", DateUtil.parse(request.getCreateTime(), DateUtil.YYYY_MM_DD)));
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
                criteria.addOrder(Order.desc("createTime"));
            }


        } catch (Exception e) {
            LoggerUtil.errorSysLog(this.getClass().getName(), "queryParam", "过滤参数异常" + e.getMessage());
        }

        return criteria;
    }
}
