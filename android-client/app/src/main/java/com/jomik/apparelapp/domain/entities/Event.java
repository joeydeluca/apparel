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

import java.util.Date;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@DatabaseTable(tableName="events")
public class Event extends Entity {
    @DatabaseField(columnName="title")
    private String title;
    @DatabaseField(columnName="location")
    private String location;
    @DatabaseField(columnName="description")
    private String description;
    @DatabaseField(columnName="start_date", dataType = DataType.DATE_STRING, format = SqlHelper.dateFormatForDbPattern)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date startDate;
    @DatabaseField(columnName="end_date", dataType = DataType.DATE_STRING, format = SqlHelper.dateFormatForDbPattern)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date endDate;
    @DatabaseField(columnName="event_type")
    private EventType eventType;

    @DatabaseField(columnName = "photo_uuid", foreign = true, foreignAutoRefresh = true)
    private Photo photo;
    @DatabaseField(columnName = "owner_uuid", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private User owner;

    @ForeignCollectionField()
    @JsonIgnore
    private ForeignCollection<EventGuest> eventGuests;

    public Event() {
    }

    public ForeignCollection<EventGuest> getEventGuests() {
        return eventGuests;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventGuests(ForeignCollection<EventGuest> eventGuests) {
        this.eventGuests = eventGuests;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Events.TITLE, getTitle());
        values.put(ApparelContract.Events.LOCATION, getLocation());
        values.put(ApparelContract.Events.DESCRIPTION, getDescription());
        values.put(ApparelContract.Events.START_DATE, SqlHelper.getDateForDb(getStartDate()));
        values.put(ApparelContract.Events.END_DATE, SqlHelper.getDateForDb(getEndDate()));
        return values;
    }
}
