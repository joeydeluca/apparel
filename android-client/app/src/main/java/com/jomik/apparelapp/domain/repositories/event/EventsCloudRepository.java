package com.jomik.apparelapp.domain.repositories.event;

import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.user.User;

import java.util.List;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public class EventsCloudRepository implements EventsRepository {

    private static EventsCloudRepository instance;

    public static EventsCloudRepository getInstance() {
        if(instance == null) {
            instance = new EventsCloudRepository();
        }
        return instance;
    }


    @Override
    public List<Event> findAll() {
        return null;
    }

    @Override
    public void save(Event event) {

    }

    @Override
    public Event findOne(String id) {
        return null;
    }

    @Override
    public void delete(Event event) {

    }

    @Override
    public List<Event> findAllForUser(User user) {
        return null;
    }
}
