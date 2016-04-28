package com.jomik.apparelapp.infrastructure.events;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class EventSearchStart {
    private String keyword;

    public EventSearchStart(String keyword) {
        this.keyword = keyword;
    }
}
