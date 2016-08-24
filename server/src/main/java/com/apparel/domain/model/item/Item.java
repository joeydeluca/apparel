package com.apparel.domain.model.item;


import com.apparel.domain.model.ApparelEntity;

import javax.persistence.*;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class Item extends ApparelEntity {

    private String name;

    private String description;

    @Column(name = "primary_color", length = 6)
    private String primaryColor;

    @Column(name = "secondary_color", length = 6)
    private String secondaryColor;

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

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
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
