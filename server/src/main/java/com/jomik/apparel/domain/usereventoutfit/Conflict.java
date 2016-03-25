package com.jomik.apparel.domain.usereventoutfit;

import com.jomik.apparel.domain.Entity;
import com.jomik.apparel.domain.item.Conflictable;

import java.util.List;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
public class Conflict extends Entity {
    List<Conflictable> conflictedProperties;
    private UserEventOutfit conflictedUserEventOutfit;
}
