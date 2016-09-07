package com.apparel.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
@Entity
public class EventGuest extends ApparelEntity {
    @ManyToOne(optional=false)
    @JoinColumn(name="eventUuid")
    @JsonBackReference
    private Event event;

    @ManyToOne(optional=false)
    @JoinColumn(name="guestUuid")
    private User guest;

    @OneToMany(mappedBy = "eventGuest", fetch = FetchType.EAGER)
    private Set<EventGuestOutfit> eventGuestOutfits = new HashSet<>();

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public Set<EventGuestOutfit> getEventGuestOutfits() {
        return eventGuestOutfits;
    }

    public void setEventGuestOutfits(Set<EventGuestOutfit> eventGuestOutfits) {
        this.eventGuestOutfits = eventGuestOutfits;
    }
}
