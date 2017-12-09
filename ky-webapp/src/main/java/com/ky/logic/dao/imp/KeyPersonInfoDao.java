package com.ky.logic.dao.imp;

import com.ky.logic.dao.IKeyPersonInfoDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.KeyPersonInfoEntity;
import com.ky.logic.model.request.KeyPersonInfoRequest;
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
@Repository("keyPersonInfoDao")
public class KeyPersonInfoDao extends AbstractHibernateDao<KeyPersonInfoEntity> implements IKeyPersonInfoDao {

    /**
     * 添加关键人
     *
     * @param entity 关键人实体
     * @throws Exception
     */
    @Override
    public void createKeyPersonInfo(KeyPersonInfoEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.save(entity);
    }


    /**
     * 删除关键人
     *
     * @param entity 关键人
     * @throws Exception
     */
    @Override
    public void deleteKeyPersonInfo(KeyPersonInfoEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.delete(entity);
    }

    /**
     * 修改关键人
     *
     * @param entity 关键人信息
     * @throws Exception
     */
    @Override
    public void updateKeyPersonInfo(KeyPersonInfoEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.update(entity);
    }

    /**
     * 查询关键人详情
     *
     * @param personId 关键人Id
     * @return 详情
     * @throws Exception
     */
    @Override
    public KeyPersonInfoEntity queryKeyPersonInfo(String personId) throws Exception {
        Session session = getCurrentSession();
        KeyPersonInfoEntity KeyPersonInfo = null;
        Criteria resultCr = session.createCriteria(KeyPersonInfoEntity.class)
                .add(Restrictions.eq("id", personId));

        List<KeyPersonInfoEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            KeyPersonInfo = list.get(0);
        }

        return KeyPersonInfo;

    }

    /**
     * 根据名字查询
     *
     * @param personName 人名
     * @return 结果
     * @throws Exception
     */
    @Override
    public KeyPersonInfoEntity queryByName(String personName) throws Exception {
        Session session = getCurrentSession();
        KeyPersonInfoEntity KeyPersonInfo = null;
        Criteria resultCr = session.createCriteria(KeyPersonInfoEntity.class)
                .add(Restrictions.eq("personName", personName));

        List<KeyPersonInfoEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            KeyPersonInfo = list.get(0);
        }

        return KeyPersonInfo;
    }

    /**
     * 分页查询关键人列表
     *
     * @param request 请求参数
     * @return 键词列表
     * @throws Exception
     */
    @Override
    public Page<KeyPersonInfoEntity> queryByPage(KeyPersonInfoRequest request) throws Exception {
        Page<KeyPersonInfoEntity> page = new Page<>();

        Session session = getCurrentSession();
        // 查询结果
        Criteria resultCr = session.createCriteria(KeyPersonInfoEntity.class);

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
        List<KeyPersonInfoEntity> list = resultCr.list();
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
     * 查询所有关键人
     *
     * @return 关键人
     * @throws Exception
     */
    @Override
    public List<KeyPersonInfoEntity> queryAll() throws Exception {

        return null;
    }

    // 查询过滤条件
    private Criteria queryParam(Criteria criteria, KeyPersonInfoRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getPersonId())) {
                criteria = criteria.add(Restrictions.eq("id", request.getPersonId()));
            }

            if (StringUtils.isNotEmpty(request.getPersonName())) {
                criteria = criteria.add(Restrictions.like("personName", request.getPersonName(), MatchMode.ANYWHERE));
            }

            if (StringUtils.isNotEmpty(request.getPersonImageAddress())) {
                criteria = criteria.add(Restrictions.like("personImageAddress", request.getPersonImageAddress(), MatchMode.ANYWHERE));
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
