package com.apparel.domain.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by Joe Deluca on 10/23/2016.
 */
@Entity
public class Conflict extends ApparelEntity {

    private Float rate;

    @OneToOne
    @JoinColumn(name="eventGuestOutfitItemUuid1")
    private EventGuestOutfitItem eventGuestOutfitItem1;

    @OneToOne
    @JoinColumn(name="eventGuestOutfitItemUuid2")
    private EventGuestOutfitItem eventGuestOutfitItem2;

    public EventGuestOutfitItem getEventGuestOutfitItem1() {
        return eventGuestOutfitItem1;
    }

    public void setEventGuestOutfitItem1(EventGuestOutfitItem eventGuestOutfitItem1) {
        this.eventGuestOutfitItem1 = eventGuestOutfitItem1;
    }

    public EventGuestOutfitItem getEventGuestOutfitItem2() {
        return eventGuestOutfitItem2;
    }

    public void setEventGuestOutfitItem2(EventGuestOutfitItem eventGuestOutfitItem2) {
        this.eventGuestOutfitItem2 = eventGuestOutfitItem2;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
