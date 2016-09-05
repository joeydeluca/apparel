
package com.jomik.apparelapp.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.User;

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
}