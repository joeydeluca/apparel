package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Event extends Entity {
    private String title;
    private String location;
    private String description;
    private String startDate;
    private String endDate;
    private String ownerUuid;
    private String ownerFacebookId;
    private String ownerName;
    private String photoUuid;
    private String photoPath;
    private String photoPathSmall;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPathSmall() {
        return photoPathSmall;
    }

    public void setPhotoPathSmall(String photoPathSmall) {
        this.photoPathSmall = photoPathSmall;
    }

    public String getOwnerFacebookId() {
        return ownerFacebookId;
    }

    public void setOwnerFacebookId(String ownerFacebookId) {
        this.ownerFacebookId = ownerFacebookId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
        values.put(ApparelContract.Events.START_DATE, getStartDate());
        values.put(ApparelContract.Events.END_DATE, getEndDate());
        values.put(ApparelContract.Events.OWNER_UUID, getOwnerUuid());
        values.put(ApparelContract.Events.PHOTO_UUID, getPhotoUuid());
        return values;
    }
}
