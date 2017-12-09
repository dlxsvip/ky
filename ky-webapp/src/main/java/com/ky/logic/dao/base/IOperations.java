package com.ky.logic.dao.base;



import com.ky.logic.utils.page.Page;

import java.io.Serializable;
import java.util.List;

/*
 * 通用的操作接口
 */
public interface IOperations<T extends Serializable> {

    T get(final long id) throws Exception;

    List<T> get() throws Exception;

    Page<T> getPage(Page<T> page) throws Exception;

    void create(final T entity) throws Exception;

    void createEntity(final T entity) throws Exception;

    void create(final List<T> entities) throws Exception;

    T update(final T entity) throws Exception;

    T merge(final T entity) throws Exception;

    void merge(final List<T> entities) throws Exception;

    void delete(final T entity) throws Exception;

    void deleteAll() throws Exception;

    void deleteById(final long entityId) throws Exception;

	T get(final String id) throws Exception;
}
