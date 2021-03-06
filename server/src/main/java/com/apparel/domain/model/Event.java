package com.apparel.domain.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class Event extends ApparelEntity {
    private String title;
    private String location;
    private String description;
    private String eventType;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DATE_FORMAT_PATTERN)
    private Date startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=DATE_FORMAT_PATTERN)
    private Date endDate;

    @OneToOne
    @JoinColumn(name = "ownerUuid")
    private User owner;

    @OneToOne
    @JoinColumn(name = "photoUuid")
    private Photo photo;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private Set<EventGuest> eventGuests = new HashSet<>();

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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public Set<EventGuest> getEventGuests() {
        return eventGuests;
    }

    public void setEventGuests(Set<EventGuest> eventGuests) {
        this.eventGuests = eventGuests;
    }
}
