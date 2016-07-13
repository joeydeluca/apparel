package com.jomik.apparelapp.domain.repositories.event;

import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe Deluca on 4/5/2016.
 */
public class EventsInMemoryRepository implements EventsRepository {
    private static EventsInMemoryRepository instance;
    private final List<Event> events = new ArrayList<>();

    private EventsInMemoryRepository() {

    }

    public static EventsInMemoryRepository getInstance() {
        if(instance == null) {
            instance = new EventsInMemoryRepository();
        }
        return instance;
    }

    @Override
    public List<Event> findAll() {
        sleepyTime();
        return Collections.unmodifiableList(events);
    }

    @Override
    public void save(Event event) {
        sleepyTime();
        if(events.contains(event)) {
            events.set(events.indexOf(event), event);
        } else {
            events.add(event);
        }
    }

    @Override
    public Event findOne(String id) {
        sleepyTime();
        for (Event event : events) {
            if(event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public void delete(Event event) {
        sleepyTime();
        events.remove(event);
    }

    @Override
    public List<Event> findAllForUser(User user) {
        List<Event> eventsForUser = new ArrayList<>();
        for (Event event : events) {
            if((event.getAttendees() != null && event.getAttendees().contains(user)) || event.getOwnerUuid().equals(user)) {
                eventsForUser.add(event);
            }
        }
        return eventsForUser;
    }

    private void sleepyTime() {
       /* try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
