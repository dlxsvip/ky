package com.ky.logic.dao.base;

import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunwei on 2016/2/26.
 */
public interface IOperationWithSessionFactory<T extends Serializable> {
    List<T> get(SessionFactory sessionFactory) ;
}
