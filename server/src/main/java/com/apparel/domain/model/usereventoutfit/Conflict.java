package com.apparel.domain.model.usereventoutfit;

import com.apparel.domain.model.ApparelEntity;
import com.apparel.domain.model.item.Conflictable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * TODO
 * Created by Joe Deluca on 3/23/2016.
 */
@Entity
public class Conflict extends ApparelEntity {
    @Transient
    List<Conflictable> conflictedProperties;
    @Transient
    private UserEventOutfit conflictedUserEventOutfit;
}
