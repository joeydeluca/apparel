package com.apparel.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class EventGuestOutfit extends ApparelEntity {
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DATE_FORMAT_PATTERN)
    private Date date;
    private String description;

    @ManyToOne
    @JoinColumn(name = "guestUuid")
    private EventGuest eventGuest;

    @OneToMany
    @JoinColumn(name = "itemUuid")
    private List<Item> items;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public EventGuest getEventGuest() {
        return eventGuest;
    }

    public void setEventGuest(EventGuest eventGuest) {
        this.eventGuest = eventGuest;
    }
}
