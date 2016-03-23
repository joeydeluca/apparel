package main.java.com.apparel.domain.event;

import main.java.com.apparel.domain.Entity;
import main.java.com.apparel.domain.user.User;

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
    private List<User> attendees;
}
