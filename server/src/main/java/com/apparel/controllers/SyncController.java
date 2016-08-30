package com.apparel.controllers;

import com.apparel.controllers.dtos.SyncDto;
import com.apparel.domain.model.EventGuest;
import com.apparel.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
@RestController
@RequestMapping("/users")
public class SyncController {

    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final EventGuestRepository eventGuestRepository;

    @Autowired
    public SyncController(final EventRepository eventRepository,
                          final ItemRepository itemRepository,
                          final PhotoRepository photoRepository,
                          final UserRepository userRepository,
                          final EventGuestRepository eventGuestRepository){
        this.eventRepository = eventRepository;
        this.itemRepository = itemRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.eventGuestRepository = eventGuestRepository;
    }


    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SyncDto> getUserData(@PathVariable("id") String uuid) {

        SyncDto dto = new SyncDto();

        try {
            dto.setUser(userRepository.findOne(uuid));
        } catch(Exception e) {}


        // find all events im attending
        Set<EventGuest> eventGuests = eventGuestRepository.findByGuestUuid(uuid);

        // get all events i own
        dto.setEvents(eventRepository.findByOwnerUuid(uuid));

        // Get items i own and items that are part of events that i am attending
        dto.setItems(itemRepository.findByUserUuid(uuid));



        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<String> setUserData(@PathVariable("id") String userId, @RequestBody SyncDto syncDto) {

        // Save
        photoRepository.save(syncDto.getPhotos());
        photoRepository.flush();
        userRepository.save(syncDto.getUser());
        itemRepository.save(syncDto.getItems());
        eventRepository.save(syncDto.getEvents());

        return ResponseEntity.ok("{\"status\":\"OK\"}");
    }

}
