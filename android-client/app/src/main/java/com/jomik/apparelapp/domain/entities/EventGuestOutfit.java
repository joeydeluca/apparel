package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class EventGuestOutfit extends Entity {
    private String date;
    private String description;
    private String eventGuestUuid;

    private User user;
    private List<Item> items;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getEventGuestUuid() {
        return eventGuestUuid;
    }

    public void setEventGuestUuid(String eventGuestUuid) {
        this.eventGuestUuid = eventGuestUuid;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.EventGuestOutfits.DESCRIPTION, getDescription());
        values.put(ApparelContract.EventGuestOutfits.EVENT_DATE, getDescription());
        values.put(ApparelContract.EventGuestOutfits.EVENT_GUEST_UUID, getEventGuestUuid());
        return values;
    }
}
