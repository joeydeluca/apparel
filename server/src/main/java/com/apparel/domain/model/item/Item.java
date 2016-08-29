package com.apparel.domain.model.item;


import com.apparel.domain.model.ApparelEntity;
import com.apparel.domain.model.photo.Photo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class Item extends ApparelEntity {
    private String name;
    private String description;
    private String itemCategory;
    private String userUuid;

    @OneToOne
    @JoinColumn(name = "photoUuid")
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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
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
}
