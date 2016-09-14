package com.jomik.apparelapp.infrastructure.events;

import com.jomik.apparelapp.domain.entities.Event;

import java.util.List;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class FindUserCirclesComplete {
    private List<Event> events;

    public FindUserCirclesComplete(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}

