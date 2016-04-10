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
public class ItemsInMemoryRepository implements ItemsRepository {
    private final List<Item> items = new ArrayList<>();

    @Override
    public List<Item> findAll() {
        return items;
    }

    {
        for(int i = 0; i < 10; i++) {
            items.add(new Item() {{
                setName("pants");
                setDescription("this is a description");
                setItemCategory(ItemCategory.BOTTOMS);
                setItemColor(ItemColor.GRAY);
                setItemPattern(ItemPattern.PLAIN);
                setPhotoId(123);
            }});
        }
    }
}
