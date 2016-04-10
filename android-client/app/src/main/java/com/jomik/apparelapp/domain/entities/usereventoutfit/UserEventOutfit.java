package com.jomik.apparelapp.domain.entities.usereventoutfit;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.outfit.Outfit;
import com.jomik.apparelapp.domain.entities.user.User;

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
