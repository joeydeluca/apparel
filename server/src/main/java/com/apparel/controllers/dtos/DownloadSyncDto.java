
package com.apparel.controllers.dtos;

import com.apparel.domain.model.Event;
import com.apparel.domain.model.Item;
import com.apparel.domain.model.User;
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
