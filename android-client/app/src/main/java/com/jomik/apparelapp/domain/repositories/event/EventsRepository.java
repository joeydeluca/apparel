package com.jomik.apparelapp.domain.repositories.event;

import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.user.User;

import java.util.List;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public interface EventsRepository {
    List<Event> findAll();
    void save(Event event);
    Event findOne(String id);
    void delete(Event event);
    List<Event> findAllForUser(User user);
}

