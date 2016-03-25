package com.jomik.apparel.domain.usereventoutfit;

import com.jomik.apparel.domain.Entity;
import com.jomik.apparel.domain.event.Event;
import com.jomik.apparel.domain.outfit.Outfit;
import com.jomik.apparel.domain.user.User;

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
