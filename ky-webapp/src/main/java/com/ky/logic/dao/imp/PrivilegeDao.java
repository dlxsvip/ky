package com.ky.logic.dao.imp;

import com.ky.logic.dao.IPrivilegeDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yl on 2017/7/24.
 */
@Repository("privilegeDao")
public class PrivilegeDao extends AbstractHibernateDao<UserPrivilegeEntity> implements IPrivilegeDao {

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createPrivilege(UserPrivilegeEntity privilege) throws Exception {
        Session session = getCurrentSession();
        session.save(privilege);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePrivilege(UserPrivilegeEntity privilege) throws Exception {
        Session session = getCurrentSession();
        session.merge(privilege);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePrivilege(String privilegeId) throws Exception {
        Session session = getCurrentSession();

        UserPrivilegeEntity entity = query(privilegeId);
        session.delete(entity);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserPrivilegeEntity query(String privilegeId) throws Exception {
        Session session = getCurrentSession();
        UserPrivilegeEntity entity = null;
        Criteria resultCr = session.createCriteria(UserPrivilegeEntity.class)
                .add(Restrictions.eq("id", privilegeId));

        List<UserPrivilegeEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserPrivilegeEntity> queryPrivilegePage(UserPrivilegeEntity privilege, Paging paging) throws Exception {
        Session session = getCurrentSession();
        Page<UserPrivilegeEntity> page = new Page<UserPrivilegeEntity>();

        //查询结果
        Criteria resultCr = session.createCriteria(UserPrivilegeEntity.class);

        // 条件查询参数
        resultCr = queryParam(resultCr, privilege, paging);

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
        List<UserPrivilegeEntity> list = resultCr.list();

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

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserPrivilegeEntity getByPrivilege(String privilege) {
        Session session = getCurrentSession();
        UserPrivilegeEntity entity = null;
        Criteria resultCr = session.createCriteria(UserPrivilegeEntity.class)
                .add(Restrictions.eq("privilege", privilege));

        List<UserPrivilegeEntity> list = resultCr.list();

        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }

    @Override
    public UserPrivilegeEntity getPrivilegeById(String privilegeId) {
        Session session = getCurrentSession();
        Criteria resultCr = session.createCriteria(UserPrivilegeEntity.class)
                .add(Restrictions.eq("id", privilegeId));


        return (UserPrivilegeEntity) resultCr.uniqueResult();
    }

    /**
     * 获取用户所有的权限
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    @Override
    public List<UserPrivilegeEntity> getByUserId(String userId) throws Exception {
        return null;
    }


    private Criteria queryParam(Criteria resultCr, UserPrivilegeEntity request, Paging paging) {

        if (StringUtils.isNotEmpty(request.getId())) {
            resultCr = resultCr.add(Restrictions.eq("id", request.getId()));
        }

        if (StringUtils.isNotEmpty(request.getPrivilegeName())) {
            resultCr = resultCr.add(Restrictions.like("privilegeName", request.getPrivilegeName(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getPrivilege())) {
            resultCr = resultCr.add(Restrictions.like("privilege", request.getPrivilege(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getDescription())) {
            resultCr = resultCr.add(Restrictions.like("description", request.getDescription(), MatchMode.ANYWHERE));
        }

        /*if (StringUtils.isNotEmpty(request.getRead())) {
            resultCr = resultCr.add(Restrictions.like("read", request.getRead(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(request.getReadDescription())) {
            resultCr = resultCr.add(Restrictions.like("readDescription", request.getReadDescription(), MatchMode.ANYWHERE));
        }


        if (StringUtils.isNotEmpty(request.getWrite())) {
            resultCr = resultCr.add(Restrictions.like("write", request.getWrite(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(request.getWriteDescription())) {
            resultCr = resultCr.add(Restrictions.like("writeDescription", request.getWriteDescription(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getOperation())) {
            resultCr = resultCr.add(Restrictions.like("operation", request.getOperation(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(request.getOperationDescription())) {
            resultCr = resultCr.add(Restrictions.like("operationDescription", request.getOperationDescription(), MatchMode.ANYWHERE));
        }*/

        if (null != request.getCreateTime()) {
            resultCr = resultCr.add(Restrictions.eq("createTime", request.getCreateTime()));
        }

        if (null != request.getUpdateTime()) {
            resultCr = resultCr.add(Restrictions.eq("updateTime", request.getUpdateTime()));
        }

        // 按字段排序
        if (StringUtils.isNotEmpty(paging.getOrderBy())) {
            if (paging.getOrderAsc()) {
                resultCr.addOrder(Order.asc(paging.getOrderBy()));
            } else {
                resultCr.addOrder(Order.desc(paging.getOrderBy()));
            }
        } else {
            // 默认按时间 降序
            resultCr.addOrder(Order.desc("createTime"));
        }
        return resultCr;
    }
}
