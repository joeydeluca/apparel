package com.jomik.apparel.domain.model.item;


import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.photo.Photo;

import javax.persistence.*;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "item", catalog = "appareldb")
public class Item extends ApparelEntity {

    private String name;

    private String description;

    @Column(name = "item_color")
    @Enumerated(EnumType.STRING)
    private ItemColor itemColor;

    @Column(name = "item_pattern")
    @Enumerated(EnumType.STRING)
    private ItemPattern itemPattern;

    @Column(name = "item_category")
    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Column(name = "photo_id")
    private String photoId;

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

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}
