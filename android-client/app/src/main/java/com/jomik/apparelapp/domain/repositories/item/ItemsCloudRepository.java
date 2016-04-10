package com.jomik.apparelapp.domain.repositories.item;

import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.item.ItemCategory;
import com.jomik.apparelapp.domain.entities.item.ItemColor;
import com.jomik.apparelapp.domain.entities.item.ItemPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public class ItemsCloudRepository implements ItemsRepository {

    @Override
    public List<Item> findAll() {
        throw new RuntimeException("not yet implemented");
    }
}
