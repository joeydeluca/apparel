package com.jomik.apparelapp.domain.entities.event;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Event extends Entity {
    private String title;
    private String location;
    private Date startDate;
    private Date endDate;
    private User owner;
    private List<UserEventOutfit> attendees = new ArrayList<>();
    private Integer photoId;

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

    public List<UserEventOutfit> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<UserEventOutfit> attendees) {
        this.attendees = attendees;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }
}
