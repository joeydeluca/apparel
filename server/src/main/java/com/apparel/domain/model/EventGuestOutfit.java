package com.apparel.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "eventGuestOutfit")
    @JsonIgnore
    private List<EventGuestOutfitItem> eventGuestOutfitItems;

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

    public List<EventGuestOutfitItem> getEventGuestOutfitItems() {
        return eventGuestOutfitItems;
    }

    public void setEventGuestOutfitItems(List<EventGuestOutfitItem> eventGuestOutfitItems) {
        this.eventGuestOutfitItems = eventGuestOutfitItems;
    }

    public EventGuest getEventGuest() {
        return eventGuest;
    }

    public void setEventGuest(EventGuest eventGuest) {
        this.eventGuest = eventGuest;
    }
}
