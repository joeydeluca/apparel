package com.jomik.apparelapp.infrastructure.rest;

import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
public class SyncDto {
    private Set<Item> items = new HashSet<>();
    private Set<Event> events = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();

    public boolean canUpload() {
        return !items.isEmpty() || !events.isEmpty() || !photos.isEmpty();
    }

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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
}
