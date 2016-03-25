package main.java.com.apparel.domain.usereventoutfit;

import main.java.com.apparel.domain.Entity;
import main.java.com.apparel.domain.event.Event;
import main.java.com.apparel.domain.outfit.Outfit;
import main.java.com.apparel.domain.user.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class UserEventOutfit extends Entity {
    private User user;
    private Event event;
    private Outfit outfit;
    private Date date;
    private List<Conflict> conflicts;
}
