package com.apparel.controllers;

import com.apparel.controllers.dtos.DownloadSyncDto;
import com.apparel.controllers.dtos.UploadSyncDto;
import com.apparel.domain.model.Event;
import com.apparel.domain.model.Item;
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
    private final EventGuestOutfitRepository eventGuestOutfitRepository;

    @Autowired
    public SyncController(final EventRepository eventRepository,
                          final ItemRepository itemRepository,
                          final PhotoRepository photoRepository,
                          final UserRepository userRepository,
                          final EventGuestRepository eventGuestRepository,
                          final EventGuestOutfitRepository eventGuestOutfitRepository){
        this.eventRepository = eventRepository;
        this.itemRepository = itemRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.eventGuestRepository = eventGuestRepository;
        this.eventGuestOutfitRepository = eventGuestOutfitRepository;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<DownloadSyncDto> getUserData(@PathVariable("id") String uuid) {

        DownloadSyncDto dto = new DownloadSyncDto();

        try {
            dto.setUser(userRepository.findOne(uuid));
        } catch(Exception e) {
            throw new IllegalArgumentException("Invalid user id");
        }

        // Get items i own
        Set<Item> items = itemRepository.findByUserUuid(uuid);
        dto.setItems(items);

        // find all events i own or am attending
        Set<Event> events = eventRepository.findByOwnerUuid(uuid);
        events.addAll(eventRepository.findByEventGuestsGuestUuid(uuid));
        dto.setEvents(events);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<String> setUserData(@PathVariable("id") String userId, @RequestBody UploadSyncDto syncDto) {

        // Save
        photoRepository.save(syncDto.getPhotos());
        photoRepository.flush();
        userRepository.save(syncDto.getUser());
        itemRepository.save(syncDto.getItems());
        eventRepository.save(syncDto.getEvents());
        eventGuestRepository.save(syncDto.getEventGuests());
        eventGuestOutfitRepository.save(syncDto.getEventGuestOutfits());

        return ResponseEntity.ok("{\"status\":\"OK\"}");
    }

}
