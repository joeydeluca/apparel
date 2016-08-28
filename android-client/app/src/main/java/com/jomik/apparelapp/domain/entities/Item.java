package com.jomik.apparelapp.domain.entities;


import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Item extends Entity {
    private String name;
    private String description;
    private ItemCategory itemCategory;
    private String photoUuid;
    private String userUuid;

    @JsonIgnore
    private Photo photo;

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

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getPhotoUuid() {
        return photoUuid;
    }

    public void setPhotoUuid(String photoUuid) {
        this.photoUuid = photoUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @JsonIgnore
    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Items.NAME, getName());
        values.put(ApparelContract.Items.DESCRIPTION, getDescription());
        values.put(ApparelContract.Items.ITEM_CATEGORY, getItemCategory().name());
        values.put(ApparelContract.Items.PHOTO_UUID, getPhotoUuid());
        return values;
    }
}
