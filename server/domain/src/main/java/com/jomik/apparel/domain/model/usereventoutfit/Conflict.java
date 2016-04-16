package com.jomik.apparel.domain.model.usereventoutfit;

import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.item.Conflictable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
@Entity
@Table(name = "conflict", catalog = "appareldb")
public class Conflict extends ApparelEntity {
    @Transient
    List<Conflictable> conflictedProperties;
    @Transient
    private UserEventOutfit conflictedUserEventOutfit;
}
