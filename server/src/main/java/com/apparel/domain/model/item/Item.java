package com.apparel.domain.model.item;


import com.apparel.domain.model.ApparelEntity;

import javax.persistence.Entity;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class Item extends ApparelEntity {
    private String name;
    private String description;
    private String itemCategory;
    private String photoUuid;
    private String userUuid;

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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
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
}
