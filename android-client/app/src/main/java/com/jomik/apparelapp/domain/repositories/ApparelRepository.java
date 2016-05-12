package com.jomik.apparelapp.domain.repositories;

import com.jomik.apparelapp.domain.entities.Entity;

import java.util.List;

/**
 * Created by Joe Deluca on 5/11/2016.
 */
public interface ApparelRepository<T extends Entity> {
    List<T> findAll();
    void save(T entity);
    T findOne(String id);
    void delete(T entity);
}
