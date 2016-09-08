
package com.apparel.controllers.dtos;

import com.apparel.domain.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by Joe Deluca on 8/24/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadSyncDto {
    private User user;
    private Set<Item> items = new HashSet<>();
    private Set<Event> events = new HashSet<>();
    private Set<EventGuest> eventGuests = new HashSet<>();
    private Set<EventGuestOutfit> eventGuestOutfits = new HashSet<>();
    private Set<EventGuestOutfitItem> eventGuestOutfitItems = new HashSet<>();

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<EventGuest> getEventGuests() {
        return eventGuests;
    }

    public void setEventGuests(Set<EventGuest> eventGuests) {
        this.eventGuests = eventGuests;
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

    public void setEventGuestOutfitItems(Set<EventGuestOutfitItem> eventGuestOutfitItems) {
        this.eventGuestOutfitItems = eventGuestOutfitItems;
    }
}
