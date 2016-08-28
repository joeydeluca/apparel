package com.apparel.controllers.dtos;

import com.apparel.domain.model.event.Event;
import com.apparel.domain.model.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.lang.model.element.ElementVisitor;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncDto {
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
}
