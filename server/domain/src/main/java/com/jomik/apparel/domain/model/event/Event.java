package com.jomik.apparel.domain.model.event;


import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "event", catalog = "appareldb")
public class Event extends ApparelEntity {
    private String title;
    private String location;
    private Date startDate;
    private Date endDate;
    @Transient
    private User owner;
    @Transient
    private List<User> attendees;
}
