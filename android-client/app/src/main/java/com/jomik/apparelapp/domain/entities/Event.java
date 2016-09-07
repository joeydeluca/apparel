package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;

import java.util.Date;
import java.util.List;

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
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=SqlHelper.dateFormatForDbPattern)
    private Date startDate;
    @DatabaseField(columnName="end_date", dataType = DataType.DATE_STRING, format = SqlHelper.dateFormatForDbPattern)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=SqlHelper.dateFormatForDbPattern)
    private Date endDate;

    @DatabaseField(columnName = "photo_uuid", foreign = true, foreignAutoRefresh = true)
    private Photo photo;
    @DatabaseField(columnName = "owner_uuid", foreign = true, foreignAutoRefresh = true)
    private User owner;

    @ForeignCollectionField()
    @JsonIgnore
    private ForeignCollection<EventGuest> eventGuests;

    @JsonProperty("eventGuests")
    private List<EventGuest> eventGuestList;

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

    public List<EventGuest> getEventGuestList() {
        return eventGuestList;
    }

    public void setEventGuestList(List<EventGuest> eventGuestList) {
        this.eventGuestList = eventGuestList;
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
