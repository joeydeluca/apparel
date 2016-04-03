package com.jomik.apparelapp.domain.event;

import com.jomik.apparelapp.domain.Entity;
import com.jomik.apparelapp.domain.user.User;

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
    private List<User> attendees;
}
