package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
@DatabaseTable(tableName="event_guests")
public class EventGuest extends Entity {
    @DatabaseField(foreign=true, columnName="event_uuid", foreignAutoRefresh = true)
    private Event event;
    @DatabaseField(foreign=true, columnName="guest_uuid", foreignAutoRefresh = true)
    private User guest;

    @ForeignCollectionField(eager = true)
    @JsonIgnore
    private ForeignCollection<EventGuestOutfit> eventGuestOutfits;

    public EventGuest() {
    }

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

    public ForeignCollection<EventGuestOutfit> getEventGuestOutfits() {
        return eventGuestOutfits;
    }

    public void setEventGuestOutfits(ForeignCollection<EventGuestOutfit> eventGuestOutfits) {
        this.eventGuestOutfits = eventGuestOutfits;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        return values;
    }
}
