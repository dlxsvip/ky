package com.ky.logic.dao.imp;

import com.ky.logic.dao.IRoleDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.utils.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
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
 * @create 2017-07-06 17:26
 * @since 1.0
 **/
@Repository("roleDao")
public class RoleDao extends AbstractHibernateDao<UserRoleEntity> implements IRoleDao {


    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createRole(UserRoleEntity role) throws Exception {
        Session session = getCurrentSession();
        session.save(role);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateRole(UserRoleEntity role) throws Exception {
        Session session = getCurrentSession();
        session.merge(role);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteRole(String roleId) throws Exception {
        Session session = getCurrentSession();

        UserRoleEntity entity = query(roleId);
        session.delete(entity);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserRoleEntity query(String roleId) throws Exception {
        Session session = getCurrentSession();
        UserRoleEntity entity = null;
        Criteria resultCr = session.createCriteria(UserRoleEntity.class)
                .setFetchMode("privilege", FetchMode.JOIN)
                .add(Restrictions.eq("id", roleId));

        entity = (UserRoleEntity) resultCr.uniqueResult();

        return entity;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Page<UserRoleEntity> queryRolePage(UserRoleEntity role, Paging paging) throws Exception {
        Session session = getCurrentSession();
        Page<UserRoleEntity> page = new Page<UserRoleEntity>();

        //查询结果
        Criteria resultCr = session.createCriteria(UserRoleEntity.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setFetchMode("privilege", FetchMode.JOIN);

        // 条件查询参数
        resultCr = queryParam(resultCr, role, paging);

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
            //此处解决使用Criteria.DISTINCT_ROOT_ENTITY 导致分页结果不准确的问题
            resultCr.setFetchSize(paging.getPageSize());
        }
        // 查询记录
        List<UserRoleEntity> list = resultCr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setFetchMode("privilege", FetchMode.JOIN).list();

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
    public UserRoleEntity getByName(String roleName) {
        Session session = getCurrentSession();
        UserRoleEntity entity = null;
        Criteria resultCr = session.createCriteria(UserRoleEntity.class)
                .setFetchMode("privilege", FetchMode.JOIN)
                .add(Restrictions.eq("roleName", roleName));

        entity = (UserRoleEntity) resultCr.uniqueResult();

        return entity;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserRoleEntity getByRole(String role) throws Exception {
        Session session = getCurrentSession();
        Criteria resultCr = session.createCriteria(UserRoleEntity.class)
                .add(Restrictions.eq("role", role))
                .setFetchMode("privilege", FetchMode.JOIN);

        return (UserRoleEntity) resultCr.uniqueResult();
    }

    @Override
    public void deletePrivilegesByRoleId(String roleId) {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("delete from user_role_user_privilege where user_role_id = '").append(roleId).append("'");

        Query sqlQuery = session.createSQLQuery(sql.toString());

        sqlQuery.executeUpdate();
    }

    @Override
    public void addPrivileges(String roleId, String[] privilegeIds) {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("insert into user_role_user_privilege (user_role_id,privilege_id) values ");

        for (int i = 0; i < privilegeIds.length; i++) {
            sql.append("('").append(roleId).append("','").append(privilegeIds[i]).append("')");
            if (i < privilegeIds.length - 1) {
                sql.append(",");
            }
        }

        Query sqlQuery = session.createSQLQuery(sql.toString());

        sqlQuery.executeUpdate();
    }

    private Criteria queryParam(Criteria resultCr, UserRoleEntity request, Paging paging) {

        if (StringUtils.isNotEmpty(request.getId())) {
            resultCr = resultCr.add(Restrictions.eq("id", request.getId()));
        }

        if (StringUtils.isNotEmpty(request.getRoleName())) {
            resultCr = resultCr.add(Restrictions.like("roleName", request.getRoleName(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getRole())) {
            resultCr = resultCr.add(Restrictions.like("role", request.getRole(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getDescription())) {
            resultCr = resultCr.add(Restrictions.like("description", request.getDescription(), MatchMode.ANYWHERE));
        }

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
