package com.jomik.apparel.domain.outfit;

import com.jomik.apparel.domain.Entity;
import com.jomik.apparel.domain.item.Item;

import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Outfit extends Entity {
    private String name;
    private String description;
    private List<Item> items;
}