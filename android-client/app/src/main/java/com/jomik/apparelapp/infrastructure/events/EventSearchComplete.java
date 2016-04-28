package com.jomik.apparelapp.infrastructure.events;

import com.jomik.apparelapp.domain.entities.event.Event;

import java.util.List;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class EventSearchComplete {
    List<Event> searchResults;

    public EventSearchComplete(List<Event> searchResults) {
        this.searchResults = searchResults;
    }

    public List<Event> getSearchResults() {
        return searchResults;
    }
}
