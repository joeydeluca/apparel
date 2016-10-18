package com.apparel.controllers;

import com.apparel.controllers.dtos.DownloadSyncDto;
import com.apparel.controllers.dtos.UploadSyncDto;
import com.apparel.domain.model.*;
import com.apparel.domain.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

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
    private final EventGuestOutfitItemRepository eventGuestOutfitItemRepository;

    @Autowired
    public SyncController(final EventRepository eventRepository,
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
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<DownloadSyncDto> getUserData(@PathVariable("id") String facebookId) {

        DownloadSyncDto dto = new DownloadSyncDto();

        User user = userRepository.findByFacebookId(facebookId);
        if(user == null) throw new UserNotCreated();

        dto.setUser(user);

        // Get items i own
        Set<Item> items = itemRepository.findByUserUuid(user.getUuid());
        dto.setItems(items);

        // find all events i own or am attending
        Set<Event> events = eventRepository.findByOwnerUuid(user.getUuid());
        events.addAll(eventRepository.findByEventGuestsGuestUuid(user.getUuid()));
        dto.setEvents(events);

        Set<EventGuest> eventGuests = eventGuestRepository.findByEventUuidIn(events.stream().map(e -> e.getUuid()).collect(Collectors.toList()));
        dto.setEventGuests(eventGuests);

        Set<EventGuestOutfit> eventGuestOutfits = eventGuestOutfitRepository.findByEventGuestUuidIn(eventGuests.stream().map(e -> e.getUuid()).collect(Collectors.toList()));
        dto.setEventGuestOutfits(eventGuestOutfits);

        Set<EventGuestOutfitItem> eventGuestOutfitItems = eventGuestOutfitItemRepository.findByEventGuestOutfitUuidIn(eventGuestOutfits.stream().map(e -> e.getUuid()).collect(Collectors.toList()));
        dto.setEventGuestOutfitItems(eventGuestOutfitItems);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<String> setUserData(@PathVariable("id") String userId, @RequestBody UploadSyncDto syncDto) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(syncDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Save
        photoRepository.save(syncDto.getPhotos());
        photoRepository.flush();
        if(syncDto.getUser() != null) {
            userRepository.save(syncDto.getUser());
        }
        itemRepository.save(syncDto.getItems());
        eventRepository.save(syncDto.getEvents());
        eventGuestRepository.save(syncDto.getEventGuests());

        if(syncDto.getEventGuestOutfits() != null && !syncDto.getEventGuestOutfits().isEmpty()) {
            eventGuestOutfitRepository.save(syncDto.getEventGuestOutfits());
        }

        if(syncDto.getEventGuestOutfitItems() != null && !syncDto.getEventGuestOutfitItems().isEmpty()) {
            eventGuestOutfitItemRepository.deleteByEventGuestOutfitUuidIn(syncDto.getEventGuestOutfits().stream().map(i -> i.getUuid()).collect(Collectors.toList()));
            eventGuestOutfitItemRepository.save(syncDto.getEventGuestOutfitItems());
        }

        return ResponseEntity.ok("{\"status\":\"OK\"}");
    }

}
