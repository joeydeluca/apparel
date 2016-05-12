package com.jomik.apparelapp.domain.repositories;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe Deluca on 5/11/2016.
 */
public class InMemoryRepository<T extends Entity> implements ApparelRepository<T> {

    private final List<T> entities = new ArrayList<T>();

    @Override
    public List<T> findAll() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public void save(T entity) {
        if(entities.contains(entity)) {
            entities.set(entities.indexOf(entities), entity);
        } else {
            entities.add(entity);
        }
    }

    @Override
    public T findOne(String id) {
        for (Entity entity : entities) {
            if(entity.getId().equals(id)) {
                return (T) entity;
            }
        }
        return null;
    }

    @Override
    public void delete(T entity) {
        entities.remove(entity);
    }
}
