package com.apparel.domain.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
@Entity
public class EventGuest extends ApparelEntity {
    @OneToOne(optional=false)
    @JoinColumn(name="uuid")
    private Event event;

    @OneToOne(optional=false)
    @JoinColumn(name="uuid", referencedColumnName="uuid")
    private User guest;

    @OneToMany(mappedBy = "eventGuest")
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
