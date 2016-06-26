package com.jomik.apparelapp.domain.entities.outfit;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.item.Item;

import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Outfit extends Entity {
    private String description;
    private List<Item> items;
    private String photoId; // This is a single photo of the user wearing their outfit

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
