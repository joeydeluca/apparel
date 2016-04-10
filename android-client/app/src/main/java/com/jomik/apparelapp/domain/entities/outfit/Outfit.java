package com.jomik.apparelapp.domain.entities.outfit;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.item.Item;

import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Outfit extends Entity {
    private String name;
    private String description;
    private List<Item> items;
}
