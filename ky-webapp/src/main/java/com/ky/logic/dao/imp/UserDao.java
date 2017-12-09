package com.ky.logic.dao.imp;

import com.ky.logic.dao.IUserDao;
import com.ky.logic.dao.base.AbstractHibernateDao;
import com.ky.logic.entity.UserEntity;
import com.ky.logic.entity.UserPrivilegeEntity;
import com.ky.logic.entity.UserRoleEntity;
import com.ky.logic.model.Paging;
import com.ky.logic.model.request.UserRequest;
import com.ky.logic.model.response.UserResponse;
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
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yyl
 * @create 2017-07-04 16:52
 * @since 1.0
 **/
@Repository("userDao")
public class UserDao extends AbstractHibernateDao<UserEntity> implements IUserDao {

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createUser(UserEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.save(entity);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUser(UserEntity entity) throws Exception {
        Session session = getCurrentSession();
        session.merge(entity);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserEntity queryUser(UserRequest request) throws Exception {
        UserEntity entity = null;
        Session session = getCurrentSession();
        Criteria resultCr = session.createCriteria(UserEntity.class);

        if (StringUtils.isNotEmpty(request.getUserId())) {
            resultCr = resultCr.add(Restrictions.eq("id", request.getUserId()));
        }

        if (StringUtils.isNotEmpty(request.getLoginName())) {
            resultCr = resultCr.add(Restrictions.eq("loginName", request.getLoginName()));
        }


        if (StringUtils.isNotEmpty(request.getNickName())) {
            resultCr = resultCr.add(Restrictions.eq("nickName", request.getNickName()));
        }

        if (StringUtils.isNotEmpty(request.getRealName())) {
            resultCr = resultCr.add(Restrictions.eq("realName", request.getRealName()));
        }

        List<UserEntity> list = resultCr.setFetchMode("role", FetchMode.JOIN).setFetchMode("privilege", FetchMode.JOIN).list();
        if (null != list && list.size() > 0) {
            entity = list.get(0);
        }

        return entity;
    }




    @Override
    public void deleteUser(String userId) throws Exception {
        Session session = getCurrentSession();

        UserEntity entity = query(userId);
        session.delete(entity);
    }

    @Override
    public UserEntity query(String userId) throws Exception {
        Session session = getCurrentSession();
        Criteria resultCr = session.createCriteria(UserEntity.class)
                .add(Restrictions.eq("id", userId));

        return (UserEntity) resultCr.setFetchMode("role", FetchMode.JOIN).setFetchMode("privilege", FetchMode.JOIN).uniqueResult();
    }

    @Override
    public Page<UserEntity> queryUserPage(UserRequest request) throws Exception {
        Session session = getCurrentSession();
        Page<UserEntity> page = new Page<UserEntity>();

        //查询结果
        Criteria resultCr = session.createCriteria(UserEntity.class);

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
        List<UserEntity> list = resultCr.list();

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

    @Override
    public List<UserEntity> getAllUserSystemInitPassword(String[] ids) throws Exception {
        return getCurrentSession().createCriteria(UserEntity.class).add(Restrictions.in("id", ids)).list();
    }

    /**
     * 获取用户所有的角色
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    @Override
    public Set<UserRoleEntity> getRolesByUserId(String userId) throws Exception {
        UserEntity entity = query(userId);

        if (null == entity) {
            return new HashSet<>();
        }

        return entity.getRole();
    }

    /**
     * 获取用户所有的角色
     *
     * @param userLoginName 用户登录名
     * @return 权限列表
     * @throws Exception
     */
    @Override
    public Set<UserRoleEntity> getRolesByUserLoginName(String userLoginName) throws Exception {

        UserRequest request = new UserRequest();
        request.setLoginName(userLoginName);
        UserEntity entity = queryUser(request);

        if (null == entity) {
            return new HashSet<>();
        }

        return entity.getRole();
    }

    /**
     * 获取用户所有的权限
     *
     * @param userId 用户id
     * @return 权限列表
     * @throws Exception
     */
    @Override
    public Set<UserPrivilegeEntity> getPrivilegesByUserId(String userId) throws Exception {
        Set<UserRoleEntity> roles = getRolesByUserId(userId);
        if (roles.isEmpty()) {
            return new HashSet<>();
        }

        Set<UserPrivilegeEntity> privileges = new HashSet<>();
        for (UserRoleEntity entity : roles) {
            Set<UserPrivilegeEntity> tmp = entity.getPrivilege();
            privileges.addAll(tmp);
        }

        return privileges;
    }

    /**
     * 获取用户所有的权限
     *
     * @param userLoginName 用户登录名
     * @return 权限列表
     * @throws Exception
     */
    @Override
    public Set<UserPrivilegeEntity> getPrivilegesByUserLoginName(String userLoginName) throws Exception {
        Set<UserRoleEntity> roles = getRolesByUserLoginName(userLoginName);
        if (roles.isEmpty()) {
            return new HashSet<>();
        }

        Set<UserPrivilegeEntity> privileges = new HashSet<>();
        for (UserRoleEntity entity : roles) {
            Set<UserPrivilegeEntity> tmp = entity.getPrivilege();
            privileges.addAll(tmp);
        }

        return privileges;
    }

    /**
     * 根据用户id 批量删除 用户和角色关联表数据
     *
     * @param userId 用户id
     */
    @Override
    public void deleteRoleByUserId(String userId) {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("delete from user_user_role where user_id = '").append(userId).append("'");

        Query sqlQuery = session.createSQLQuery(sql.toString());

        sqlQuery.executeUpdate();
    }

    /**
     * 根据用户id 批量添加 用户和角色关联表数据
     *
     * @param userId  用户id
     * @param roleIds 权限ids
     */
    @Override
    public void addRole(String userId, String[] roleIds) {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("insert into user_user_role (user_id,role_id) values ");

        for (int i = 0; i < roleIds.length; i++) {
            sql.append("('").append(userId).append("','").append(roleIds[i]).append("')");
            if (i < roleIds.length - 1) {
                sql.append(",");
            }
        }

        Query sqlQuery = session.createSQLQuery(sql.toString());

        sqlQuery.executeUpdate();
    }

    /**
     * 查询 此角色有多少人拥有
     *
     * @param roleId 角色
     * @return 人数
     */
    @Override
    public Long queryUserNumByRoleId(String roleId) {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from user_user_role where role_id = '").append(roleId).append("'");

        Query sqlQuery = session.createSQLQuery(sql.toString());
        Long account = Long.valueOf(sqlQuery.uniqueResult().toString());

        return account;
    }

    @Override
    public List<UserResponse> queryUsersByRoleId(String roleId, Paging paging) throws Exception {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT u.id as userId, u.login_name as loginName, u.nick_name as nickName, " +
                "u.real_name as realName, u.cell_phone_num as cellphoneNum , u.tele_phone_num as telephoneNum, " +
                "u.email , date_format(u.create_time,'%Y-%m-%d %T') as createTime, u.description, date_format(u.login_time,'%Y-%m-%d %T') as loginTime ")
                .append(" FROM user u LEFT JOIN user_user_role uo ON u.id = uo.user_id ")
                .append(" where uo.role_id = '").append(roleId).append("'");

        Query sqlQuery = session.createSQLQuery(sql.toString());
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(UserResponse.class));
        sqlQuery.setFirstResult(paging.getPageSize() * (paging.getPageNum() - 1));
        sqlQuery.setMaxResults(paging.getPageSize());
        List<UserResponse> list = sqlQuery.list();


        return list;
    }

    /**
     * 解除某个用户的禁闭时间
     *
     * @param ctrlId 用户控制策略id
     */
    @Override
    public void closeForbidTime(String ctrlId) throws Exception {
        Session session = getCurrentSession();

        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE user_login_ctrl SET last_attempt_login_time = NULL WHERE id ='").append(ctrlId).append("'");

        Query sqlQuery = session.createSQLQuery(sql.toString());

        sqlQuery.executeUpdate();
    }

    private Criteria queryParam(Criteria resultCr, UserRequest request) {

        if (StringUtils.isNotEmpty(request.getUserId())) {
            resultCr = resultCr.add(Restrictions.eq("id", request.getUserId()));
        }
        if (StringUtils.isNotEmpty(request.getLoginName())) {
            resultCr = resultCr.add(Restrictions.like("loginName", request.getLoginName(), MatchMode.ANYWHERE));
        }
        // 非super 用户 查询，过滤掉super
        if (null != request.getIsSuper() && !request.getIsSuper()) {
            resultCr = resultCr.add(Restrictions.ne("loginName", "super"));
        }


        if (StringUtils.isNotEmpty(request.getNickName())) {
            resultCr = resultCr.add(Restrictions.like("nickName", request.getNickName(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getRealName())) {
            resultCr = resultCr.add(Restrictions.like("realName", request.getRealName(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getEmail())) {
            resultCr = resultCr.add(Restrictions.like("email", request.getEmail(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(request.getCellphoneNum())) {
            resultCr = resultCr.add(Restrictions.like("cellphoneNum", request.getCellphoneNum(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotEmpty(request.getCellphoneNum())) {
            resultCr = resultCr.add(Restrictions.like("telephoneNum", request.getCellphoneNum(), MatchMode.ANYWHERE));
        }

        // 按字段排序
        if (StringUtils.isNotEmpty(request.getOrderBy())) {
            if (request.getOrderAsc()) {
                resultCr.addOrder(Order.asc(request.getOrderBy()));
            } else {
                resultCr.addOrder(Order.desc(request.getOrderBy()));
            }
        } else {
            // 默认按时间 降序
            resultCr.addOrder(Order.desc("createTime"));
        }
        return resultCr;
    }

}
