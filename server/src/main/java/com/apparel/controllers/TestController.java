package com.apparel.controllers;

import com.apparel.domain.model.Event;
import com.apparel.domain.model.EventGuest;
import com.apparel.domain.model.EventGuestOutfit;
import com.apparel.domain.model.EventGuestOutfitItem;
import com.apparel.domain.repository.*;
import com.apparel.domain.service.ImageCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final EventGuestRepository eventGuestRepository;
    private final EventGuestOutfitRepository eventGuestOutfitRepository;
    private final EventGuestOutfitItemRepository eventGuestOutfitItemRepository;

    @Autowired
    private ImageCompareService imageCompareService;

    @Autowired
    public TestController(final EventRepository eventRepository,
                          final ItemRepository itemRepository,
                          final PhotoRepository photoRepository,
                          final UserRepository userRepository,
                          final EventGuestRepository eventGuestRepository,
                          final EventGuestOutfitRepository eventGuestOutfitRepository,
                          final EventGuestOutfitItemRepository eventGuestOutfitItemRepository){
        this.eventRepository = eventRepository;
        this.itemRepository = itemRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.eventGuestRepository = eventGuestRepository;
        this.eventGuestOutfitRepository = eventGuestOutfitRepository;
        this.eventGuestOutfitItemRepository = eventGuestOutfitItemRepository;
    }

    @RequestMapping(
            value = "/compare",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Void> testCompare(
            @RequestParam(name  = "uuid") String eventGuestOutfitUuid) {

        EventGuestOutfit eventGuestOutfit =  eventGuestOutfitRepository.getOne(eventGuestOutfitUuid);

        imageCompareService.compareOutfits(eventGuestOutfit);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Event> test() {
        Event event = new Event();
        event.setUuid(UUID.randomUUID().toString());
        event.setTitle("my event");

        EventGuest eventGuest = new EventGuest();
        eventGuest.setUuid(UUID.randomUUID().toString());
        eventGuest.setEvent(event);
        Set<EventGuestOutfit> eventGuestOutfits = new HashSet<>();

        eventGuest.setEventGuestOutfits(eventGuestOutfits);
        Set<EventGuest> eventGuests = new HashSet<>();
        eventGuests.add(eventGuest);

        event.setEventGuests(eventGuests);

        EventGuestOutfit eventGuestOutfit = new EventGuestOutfit();
        eventGuestOutfit.setUuid(UUID.randomUUID().toString());
        //eventGuestOutfit.setEventGuest(eventGuest);
        eventGuestOutfits.add(eventGuestOutfit);

        EventGuestOutfitItem eventGuestOutfitItem = new EventGuestOutfitItem();
        eventGuestOutfitItem.setUuid("111");
        eventGuestOutfitItem.setEventGuestOutfit(eventGuestOutfit);

        List<EventGuestOutfitItem> items = new ArrayList<>();
        items.add(eventGuestOutfitItem);

        eventGuestOutfit.setEventGuestOutfitItems(items);

        /*eventGuestOutfitRepository.save(eventGuestOutfit);

        List outfits = eventGuestOutfitRepository.findAll();
*/

        return ResponseEntity.ok(event);
    }

}
