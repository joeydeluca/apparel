package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.common.DateDeserializer;
import com.jomik.apparelapp.infrastructure.common.DateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@DatabaseTable(tableName="event_guest_outfits")
public class EventGuestOutfit extends Entity {
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @DatabaseField(columnName="event_date", dataType = DataType.DATE_STRING, format = SqlHelper.dateFormatForDbPattern)
    private Date date;
    @DatabaseField(columnName="description")
    private String description;
    @DatabaseField(foreign=true, columnName="event_guest_uuid", foreignAutoRefresh = true)
    private EventGuest eventGuest;
    @ForeignCollectionField(eager = true)
    @JsonIgnore
    private ForeignCollection<EventGuestOutfitItem> eventGuestOutfitItems;

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

    @JsonIgnore
    public ForeignCollection<EventGuestOutfitItem> getEventGuestOutfitItems() {
        return eventGuestOutfitItems;
    }

    @JsonIgnore
    public List<EventGuestOutfitItem> getEventGuestOutfitItemList() {
        List<EventGuestOutfitItem> eventGuestOutfitItemList = new ArrayList<>();

        if(eventGuestOutfitItems != null) {
            for (EventGuestOutfitItem eventGuestOutfitItem : eventGuestOutfitItems) {
                eventGuestOutfitItemList.add(eventGuestOutfitItem);
            }
        }
        return eventGuestOutfitItemList;
    }

    public void setEventGuestOutfitItems(ForeignCollection<EventGuestOutfitItem> eventGuestOutfitItems) {
        this.eventGuestOutfitItems = eventGuestOutfitItems;
    }

    @JsonIgnore
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for(EventGuestOutfitItem eventGuestOutfitItem : eventGuestOutfitItems) {
            if(eventGuestOutfitItem.getItem() != null && !eventGuestOutfitItem.getItem().isMarkedForDelete()) {
                items.add(eventGuestOutfitItem.getItem());
            }
        }
        return items;
    }

    public EventGuest getEventGuest() {
        return eventGuest;
    }

    public void setEventGuest(EventGuest eventGuest) {
        this.eventGuest = eventGuest;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.EventGuestOutfits.DESCRIPTION, getDescription());
        values.put(ApparelContract.EventGuestOutfits.EVENT_DATE, getDescription());
        return values;
    }
}
