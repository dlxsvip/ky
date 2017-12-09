package com.ky.logic.dao.base;

import com.google.common.base.Preconditions;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.page.Page;
import org.hibernate.*;
import org.hibernate.criterion.Projections;

import javax.annotation.Resource;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDao<T extends Serializable> extends  AbstractDaoWithFactoryPara<T> implements IOperations<T> {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public AbstractHibernateDao() {
    }

    protected  void setClazz(final Class<T> clazzToSet) {
        this.clazz = Preconditions.checkNotNull(clazzToSet);
    }

    public AbstractHibernateDao(Class<T> clazz) {
        super(clazz);
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public  T get(final long id) throws Exception {
        Session session = openSession();
        T entity = null;
		try {
			session.beginTransaction();
			entity = (T) session.get(clazz, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
		    session.close();
		}
        return entity;
    }

    @Override
    public T get(final String id) throws Exception {
        Session session = openSession();
        T entity = null;
		try {
			session.beginTransaction();
			entity = (T) session.get(clazz, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
		    session.close();
		}
        return entity;
    }

    public T getWithoutTransaction(Session session, final String id) throws Exception {
        T entity = null;
        try {
            entity = (T) session.get(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e ;
        }

        return entity;
    }

    @Override
    public List<T> get() throws Exception {
        Session session = openSession();
        List<T> tmpList = new ArrayList<T>();
         try {
			session.beginTransaction();
			tmpList = session.createQuery("from " + clazz.getName()).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		}finally {
            session.close();
        }
        
        return tmpList;
    }

    @Override
    public Page<T> getPage(Page<T> page) throws Exception {

        Session session = openSession();
        try {
			int totalPages, totalRows;
			totalRows = queryForCount(page,session);
			if (page.getPageSize() == 0) {
			    totalPages = totalRows;
			} else {
			    if (totalRows % page.getPageSize() == 0)
			        totalPages = totalRows / page.getPageSize();
			    else
			        totalPages = totalRows / page.getPageSize() + 1;
			}
			page.setTotalPages(totalPages);
			page.setTotalRows(totalRows);
            page.setResult(queryForResult(page,session));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
			session.close();
		}

        return page;
    }

    @Override
    public  T update(final T entity) throws Exception {
        Session session = openSession();
        try {
            session.beginTransaction();
			session.update(entity);
			session.getTransaction().commit();
		} catch(StaleStateException exception){
            LoggerUtil.printStackTrace(this.getClass().getName(), "update", exception);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
			session.close();
		}
        
        return entity;
    }

    public  T updateWithoutTransaction(Session session, final T entity) throws Exception {
        try {
            session.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw e ;
        }

        return entity;
    }

    @Override
    public  void create(final T entity) throws Exception {
        Session session = openSession();
        try {
			session.beginTransaction();
			session.persist(entity);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
            session.close();
		}
    }

    /**
     * 该方法主要是为了解决重试写数据库时，持久化类会存在id的值，当id的策略设为自动增长时会抛异常。
     * @param entity
     */
    @Override
    public void createEntity(final T entity) throws Exception {
        String methodName = "createEntity";
        Session session = openSession();
        resetGeneratedValue(entity);
        try {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LoggerUtil.printStackTrace(this.getClass().getName(), methodName, e);
            throw e ;
        } finally {
            session.close();
        }
    }

    private void resetGeneratedValue(T entity) {
        Field[] field = entity.getClass().getDeclaredFields();
        if(field == null) {
            return;
        }

        for(Field fie : field){
            if(!fie.isAccessible()){
                fie.setAccessible(true);
            }
            GeneratedValue annon = fie.getAnnotation(GeneratedValue.class);
            if (null != annon) {
                try {
                    fie.set(entity, null);
                } catch (Exception e) {
                    LoggerUtil.printStackTrace(this.getClass().getName(), "resetGeneratedValue: "+fie.getName(), e);
                }
            }
        }
    }

    @Override
    public void create(List<T> entities) throws Exception {
        Session session = sessionFactory.openSession();
        try {
			Transaction tx = session.beginTransaction();
			for (T entity : entities) {
			    session.save(entity);
			}

			tx.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
            session.close();
		}
    }

    @Override
    public  T merge(final T entity) throws Exception {
        Session session = openSession();
        try {
			session.beginTransaction();
			session.merge(entity);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
            session.close();
		}
        return entity;
    }

    @Override
    public void merge(List<T> entities) throws Exception {
        Session session = sessionFactory.openSession();
        try {
			Transaction tx = session.beginTransaction();
			for (T entity : entities) {
			    session.merge(entity);
			}

			tx.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
            session.close();
		}
    }

    @Override
    public  void delete(final T entity) throws Exception {
        Session session = openSession();
        try {
			session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
            session.close();
		}
    }

    @Override
    public  void deleteById(final long entityId) throws Exception {
        try {
			final T entity = get(entityId);
			Preconditions.checkState(entity != null);
			delete(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		}
    }


    @Override
    public void deleteAll() throws Exception {
        String hql = "delete from " + clazz.getName();
        Session session = openSession();
		try {
			
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw e ;
		} finally {
            session.close();
		}
    }

    private Criteria buildHibernateCriteria(Page<T> page,Session session) {
        Criteria cr = session.createCriteria(clazz);

        if (null != page.getFetchModeList() && 0 != page.getFetchModeList().size()) {
            for (String fetchMode : page.getFetchModeList()) {
                cr.setFetchMode(fetchMode, FetchMode.SELECT);
            }
        }
        page.buildCriteria(cr);

        return cr;
    }

    private List<T> queryForResult(Page<T> page,Session session) {
        Criteria cr = buildHibernateCriteria(page,session);

        if (0 != page.getStartRow()) {
            cr.setFirstResult(page.getStartRow());
        }
        if (0 != page.getPageSize()) {
            if (null == page.getDistinctColumn()) {
                cr.setMaxResults(page.getPageSize());
            } else {
                cr.setFetchSize(page.getPageSize());
            }
        }

        List<T> result = cr.list();

        return result;
    }

    private Integer queryForCount(Page<T> page,Session session) {
        Criteria cr = buildHibernateCriteria(page,session);
        if (null == page.getDistinctColumn()) {
            cr.setProjection(Projections.rowCount());
        } else {
            cr.setProjection(Projections.countDistinct(page.getDistinctColumn()));
        }

        return Integer.parseInt(cr.list().get(0).toString());
    }

    public Integer getCount(Page<T> page) {
        Session session = openSession();
        Integer count = 0;
        try {
            count = queryForCount(page, session);
        } catch (Exception e) {
            LoggerUtil.printStackTrace(this.getClass().getName(), "getCount", e);
        } finally {
            session.close();
        }

        return count;
    }
}
