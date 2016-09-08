
package com.jomik.apparelapp.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventGuestOutfit;
import com.jomik.apparelapp.domain.entities.EventGuestOutfitItem;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.domain.entities.User;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by Joe Deluca on 8/24/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadSyncDto {
    private User user;
    private Set<Item> items = new HashSet<>();
    private Set<Event> events = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();
    private Set<EventGuest> eventGuests = new HashSet<>();
    private Set<EventGuestOutfit> eventGuestOutfits = new HashSet<>();
    private Set<EventGuestOutfitItem> eventGuestOutfitItems = new HashSet<>();

    public boolean canUpload() {
        return user != null || !items.isEmpty() || !events.isEmpty() || !photos.isEmpty() || !eventGuests.isEmpty() || !eventGuestOutfits.isEmpty() || !eventGuestOutfitItems.isEmpty();
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<EventGuest> getEventGuests() {
        return eventGuests;
    }

    public void setEventGuests(Set<EventGuest> eventGuests) {
        this.eventGuests = eventGuests;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<EventGuestOutfit> getEventGuestOutfits() {
        return eventGuestOutfits;
    }

    public void setEventGuestOutfits(Set<EventGuestOutfit> eventGuestOutfits) {
        this.eventGuestOutfits = eventGuestOutfits;
    }

    public Set<EventGuestOutfitItem> getEventGuestOutfitItems() {
        return eventGuestOutfitItems;
    }

    public void setEventGuestOutfitItems(Set<EventGuestOutfit> eventGuestOutfits) {
        Set<EventGuestOutfitItem> eventGuestOutfitItems = new HashSet<>();

        for(EventGuestOutfit eventGuestOutfit : eventGuestOutfits) {
            eventGuestOutfitItems.addAll(eventGuestOutfit.getEventGuestOutfitItemList());
        }

        this.eventGuestOutfitItems = eventGuestOutfitItems;
    }
}
