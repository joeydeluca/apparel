package com.jomik.apparelapp.domain.repositories.item;

import com.jomik.apparelapp.domain.entities.item.Item;

import java.util.List;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public interface ItemsRepository {
    List<Item> findAll();
    void save(Item item);
    Item findOne(String id);
}

