package com.apparel.controllers;

import com.apparel.controllers.dtos.DownloadSyncDto;
import com.apparel.controllers.dtos.UploadSyncDto;
import com.apparel.domain.repository.EventRepository;
import com.apparel.domain.repository.ItemRepository;
import com.apparel.domain.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public SyncController(final EventRepository eventRepository,
                          final ItemRepository itemRepository,
                          final PhotoRepository photoRepository){
        this.eventRepository = eventRepository;
        this.itemRepository = itemRepository;
        this.photoRepository = photoRepository;
    }


    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<DownloadSyncDto> getUserData(@PathVariable("id") String uuid) {

        DownloadSyncDto dto = new DownloadSyncDto();

        // Get items i own and items that are part of events that i am attending
        dto.setItems(itemRepository.findByUserUuid(uuid));

        dto.setEvents(eventRepository.findByOwnerUuid(uuid));

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
        itemRepository.save(syncDto.getItems().stream().map(i -> i.toEntity()).collect(Collectors.toList()));
        eventRepository.save(syncDto.getEvents().stream().map(e -> e.toEntity()).collect(Collectors.toList()));

        return ResponseEntity.ok("{\"status\":\"OK\"}");
    }

}
