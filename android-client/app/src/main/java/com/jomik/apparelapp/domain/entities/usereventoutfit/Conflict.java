package com.jomik.apparelapp.domain.entities.usereventoutfit;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.item.Conflictable;

import java.util.List;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
public class Conflict extends Entity {
    List<Conflictable> conflictedProperties;
    private UserEventOutfit conflictedUserEventOutfit;
}
