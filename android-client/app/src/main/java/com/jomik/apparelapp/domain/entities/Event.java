package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;

import java.util.Date;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Event extends Entity {
    private String title;
    private String location;
    private String description;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=SqlHelper.dateFormatForDbPattern)
    private Date startDate;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=SqlHelper.dateFormatForDbPattern)
    private Date endDate;
    private String ownerUuid;
    private String photoUuid;

    private Photo photo;

    @JsonIgnore
    private User owner;

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

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getPhotoUuid() {
        return photoUuid;
    }

    public void setPhotoUuid(String photoUuid) {
        this.photoUuid = photoUuid;
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

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Events.TITLE, getTitle());
        values.put(ApparelContract.Events.LOCATION, getLocation());
        values.put(ApparelContract.Events.DESCRIPTION, getDescription());
        values.put(ApparelContract.Events.START_DATE, SqlHelper.getDateForDb(getStartDate()));
        values.put(ApparelContract.Events.END_DATE, SqlHelper.getDateForDb(getEndDate()));
        values.put(ApparelContract.Events.OWNER_UUID, getOwnerUuid());
        values.put(ApparelContract.Events.PHOTO_UUID, getPhotoUuid());
        return values;
    }
}
