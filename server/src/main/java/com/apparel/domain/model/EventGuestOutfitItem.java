package com.apparel.domain.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
@Entity
public class EventGuestOutfitItem extends ApparelEntity {
    @OneToOne
    @JoinColumn(name = "event_guest_outfit_uuid")
    private EventGuestOutfit eventGuestOutfit;
    @OneToOne
    @JoinColumn(name = "item_uuid")
    private Item item;

    public EventGuestOutfitItem() {
    }

    public EventGuestOutfit getEventGuestOutfit() {
        return eventGuestOutfit;
    }

    public void setEventGuestOutfit(EventGuestOutfit eventGuestOutfit) {
        this.eventGuestOutfit = eventGuestOutfit;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
