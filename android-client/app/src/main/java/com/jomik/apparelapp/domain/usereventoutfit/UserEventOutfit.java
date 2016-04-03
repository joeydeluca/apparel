package com.jomik.apparelapp.domain.usereventoutfit;

import com.jomik.apparelapp.domain.Entity;
import com.jomik.apparelapp.domain.event.Event;
import com.jomik.apparelapp.domain.outfit.Outfit;
import com.jomik.apparelapp.domain.user.User;

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
