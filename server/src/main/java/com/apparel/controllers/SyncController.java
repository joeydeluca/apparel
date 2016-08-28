package com.apparel.controllers;

import com.apparel.controllers.dtos.SyncDto;
import com.apparel.domain.repository.EventRepository;
import com.apparel.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
@RestController
@RequestMapping("/users")
public class SyncController {

    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public SyncController(final EventRepository eventRepository, final ItemRepository itemRepository){
        this.eventRepository = eventRepository;
        this.itemRepository = itemRepository;
    }


    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SyncDto> getUserData(@PathVariable("id") String uuid) {

        SyncDto dto = new SyncDto();

        dto.setItems(itemRepository.findByUserUuid(uuid));

        dto.setEvents(eventRepository.findByOwnerUuid(uuid));

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Void> setUserData(@PathVariable("id") String userId, @RequestBody SyncDto syncDto) {

        // Save
        itemRepository.save(syncDto.getItems());
        eventRepository.save(syncDto.getEvents());

        return ResponseEntity.ok().build();
    }

}
