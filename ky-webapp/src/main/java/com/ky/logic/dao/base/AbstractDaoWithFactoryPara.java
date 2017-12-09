package com.ky.logic.dao.base;

import com.google.common.base.Preconditions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;


public class AbstractDaoWithFactoryPara<T extends Serializable> implements IOperationWithSessionFactory<T> {
    protected Class<T> clazz;

    public AbstractDaoWithFactoryPara(){} ;
    public AbstractDaoWithFactoryPara(Class<T> clazz) {
        this.clazz = Preconditions.checkNotNull(clazz);
    }

    public List<T> get( SessionFactory sessionFactory ){
        List<T>  resultList = null ;
        try{
            Session session = sessionFactory.openSession() ;
            try {
                String entityName = clazz.getName() ;
                resultList = session.createQuery("from " + entityName).list();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw e ;
            }finally {
                session.close();
            }
        }
        catch (Exception e ){
        }
        return resultList ;
    }
}
