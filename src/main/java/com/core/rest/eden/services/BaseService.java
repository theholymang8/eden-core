package com.core.rest.eden.services;

import java.util.List;

public interface BaseService <T, ID>{

    T create (T entity);

    List<T> createAll(List<T> entities);

    List<T> createAll(T... entities);

    void update(T entity);

    void delete(T entity);

    void deleteById(ID id);

    boolean exists(T entity);

    T find(ID id);

    List<T> findAll();

}
