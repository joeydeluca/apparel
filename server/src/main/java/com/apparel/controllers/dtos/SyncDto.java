package com.apparel.controllers.dtos;

import com.apparel.domain.model.item.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
public class SyncDto {
    private Set<Item> items = new HashSet<>();
    
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
