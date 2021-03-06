/*
package com.apparel.controllers.dtos;

import com.apparel.domain.model.Event;
import com.apparel.domain.model.Photo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

*/
/**
 * Created by Joe Deluca on 8/28/2016.
 *//*

public class EventDto extends EntityDto {
    private String title;
    private String location;
    private String description;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date endDate;
    private String ownerUuid;
    private String photoUuid;

    private Photo photo;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Event toEntity() {
        Event event = new Event();
        super.toEntity(event);
        event.setTitle(title);
        event.setLocation(location);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setOwnerUuid(ownerUuid);
        Photo photo = new Photo();
        photo.setUuid(photoUuid);
        event.setPhoto(photo);
        return event;
    }
}
*/
