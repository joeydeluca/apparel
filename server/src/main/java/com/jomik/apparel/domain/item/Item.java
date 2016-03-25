package com.jomik.apparel.domain.item;

import com.jomik.apparel.domain.Entity;
import com.jomik.apparel.domain.photo.Photo;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Item extends Entity {
    private String name;
    private String description;
    private ItemColor itemColor;
    private ItemPattern itemPattern;
    private ItemCategory itemCategory;
    private Photo photo;
}
