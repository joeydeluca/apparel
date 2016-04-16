package com.jomik.apparelapp.domain.repositories.item;

import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.item.ItemCategory;
import com.jomik.apparelapp.domain.entities.item.ItemColor;
import com.jomik.apparelapp.domain.entities.item.ItemPattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public class ItemsInMemoryRepository implements ItemsRepository {
    private static ItemsInMemoryRepository instance;
    private final List<Item> items = new ArrayList<>();

    private ItemsInMemoryRepository() {

    }

    public static ItemsInMemoryRepository getInstance() {
        if(instance == null) {
            instance = new ItemsInMemoryRepository();
        }
        return instance;
    }

    @Override
    public List<Item> findAll() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public void save(Item item) {
        if(items.contains(item)) {
            items.set(items.indexOf(item), item);
        } else {
            items.add(item);
        }
    }

    @Override
    public Item findOne(String id) {
        for (Item item : items) {
            if(item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
