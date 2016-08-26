package com.jomik.apparelapp.infrastructure.rest;

import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;

import java.util.Set;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
public class SyncDto {
    private Set<Item> items;
    private Set<Photo> photos;

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }
}
