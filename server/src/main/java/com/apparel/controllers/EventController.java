package com.apparel.controllers;

import com.apparel.controllers.dtos.SyncDto;
import com.apparel.domain.model.event.Event;
import com.apparel.domain.repository.EventRepository;
import com.apparel.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public EventController(final EventRepository eventRepository, final ItemRepository itemRepository){
        this.eventRepository = eventRepository;
        this.itemRepository = itemRepository;
    }


    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<List<Event>> findEvents() {
        List<Event> retEvents = new ArrayList<>();

        List<Event> allEvent = eventRepository.findAll();
        Date now = new Date();
        for(Event event : allEvent) {
            if(!event.isMarkedForDelete() && now.getTime() <= event.getEndDate().getTime()) {
                retEvents.add(event);
            }
        }

        return ResponseEntity.ok(retEvents);
    }

}
