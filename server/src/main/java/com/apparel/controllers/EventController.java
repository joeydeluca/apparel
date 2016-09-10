package com.apparel.controllers;

import com.apparel.domain.model.Event;
import com.apparel.domain.repository.EventRepository;
import com.apparel.domain.specifications.EventSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(final EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }


    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<List<Event>> findEvents(@RequestParam("keyword") String keyword) {

        List<Specification<Event>> specifications = new ArrayList<>();

        specifications.add((EventSpecification.isNotDeleted()));

        if(keyword != null && keyword.length() > 0) {
            specifications.add((EventSpecification.containsTitle(keyword)));
        }

        Specification<Event> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specifications.where(result).and(specifications.get(i));
        }

        List<Event> events = eventRepository.findAll(result, getPageRequest()).getContent();

        return ResponseEntity.ok(events);
    }

    private PageRequest getPageRequest() {
        return new PageRequest(0, 10, Sort.Direction.DESC, "start_date");
    }




}
