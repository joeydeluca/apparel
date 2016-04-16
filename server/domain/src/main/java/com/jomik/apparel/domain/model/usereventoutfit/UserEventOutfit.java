package com.jomik.apparel.domain.model.usereventoutfit;

import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.event.Event;
import com.jomik.apparel.domain.model.outfit.Outfit;
import com.jomik.apparel.domain.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "user_event_outfit", catalog = "appareldb")
public class UserEventOutfit extends ApparelEntity {
    @Transient
    private User user;
    @Transient
    private Event event;
    @Transient
    private Outfit outfit;
    @Transient
    private Date eventDate;
    @Transient
    private List<Conflict> conflicts;
}
