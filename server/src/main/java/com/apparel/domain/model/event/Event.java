package com.apparel.domain.model.event;


import com.apparel.domain.model.ApparelEntity;
import com.apparel.domain.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class Event extends ApparelEntity {
    private String title;
    private String location;
    private Date startDate;
    private Date endDate;
    @Transient
    private User owner;
    @Transient
    private List<User> attendees;

    public Event() {
    }
}
