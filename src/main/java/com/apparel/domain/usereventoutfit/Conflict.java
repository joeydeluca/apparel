package main.java.com.apparel.domain.usereventoutfit;

import main.java.com.apparel.domain.Entity;
import main.java.com.apparel.domain.item.Conflictable;

import java.util.List;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
public class Conflict extends Entity {
    List<Conflictable> conflictedProperties;
    private UserEventOutfit conflictedUserEventOutfit;
}
