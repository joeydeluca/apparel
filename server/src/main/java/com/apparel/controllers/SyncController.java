package com.apparel.controllers;

import com.apparel.controllers.dtos.SyncDto;
import com.apparel.domain.model.item.Item;
import com.apparel.domain.repository.ItemRepository;
import com.apparel.domain.service.interfaces.IUserService;
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

    private final IUserService userService;
    private final ItemRepository itemRepository;

    @Autowired
    public SyncController(final IUserService userService, final ItemRepository itemRepository){
        this.userService = userService;
        this.itemRepository = itemRepository;
    }


    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<SyncDto> getUserData(@PathVariable("id") String uuid) {

        SyncDto dto = new SyncDto();

        Set<Item> items = itemRepository.findByUserUuid(uuid);

        dto.setItems(items);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Void> setUserData(@PathVariable("id") String userId, @RequestBody SyncDto syncDto) {

        // Save

        return ResponseEntity.ok(null);
    }

}
