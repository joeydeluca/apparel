package com.jomik.apparelapp.domain.entities;


import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@DatabaseTable(tableName="items")
public class Item extends Entity {
    @DatabaseField(columnName="name")
    private String name;
    @DatabaseField(columnName="description")
    private String description;
    @DatabaseField(columnName="item_category")
    private ItemCategory itemCategory;
    @DatabaseField(columnName = "user_uuid", foreign = true, foreignAutoRefresh = true)
    private User user;
    @DatabaseField(columnName = "photo_uuid", foreign = true, foreignAutoRefresh = true)
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Items.NAME, getName());
        values.put(ApparelContract.Items.DESCRIPTION, getDescription());
        values.put(ApparelContract.Items.ITEM_CATEGORY, getItemCategory().name());
        return values;
    }
}
