package com.jomik.apparelapp.domain.entities.item;


import com.jomik.apparelapp.domain.entities.Entity;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Item extends Entity {
    private String name;
    private String description;
    private ItemColor itemColor;
    private ItemPattern itemPattern;
    private ItemCategory itemCategory;
    private Integer photoId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemColor getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColor itemColor) {
        this.itemColor = itemColor;
    }

    public ItemPattern getItemPattern() {
        return itemPattern;
    }

    public void setItemPattern(ItemPattern itemPattern) {
        this.itemPattern = itemPattern;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }
}
