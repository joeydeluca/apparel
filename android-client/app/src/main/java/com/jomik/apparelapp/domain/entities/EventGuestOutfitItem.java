package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
@DatabaseTable(tableName="event_guest_outfit_items")
public class EventGuestOutfitItem extends Entity {
    @DatabaseField(foreign=true, columnName="event_guest_outfit_uuid")
    private EventGuestOutfit eventGuestOutfit;
    @DatabaseField(columnName = "item_uuid", foreign = true, foreignAutoRefresh = true)
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

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();

        return values;
    }
}
