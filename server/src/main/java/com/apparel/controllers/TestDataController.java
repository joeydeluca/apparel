package com.apparel.controllers;

import com.apparel.domain.model.Event;
import com.apparel.domain.model.Item;
import com.apparel.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * An easy way to add some user data.
 *
 * Created by Mick on 4/16/2016.
 */
@RestController
public class TestDataController {

    private final ItemRepository itemRepository;

    @Autowired
    public TestDataController(final ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    

    @RequestMapping(
            value = "/addrecord",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Item> populateTestData(){
        Item item = new Item();
        item.setUuid(UUID.randomUUID().toString());
        item.setItemCategory("TOPS");
        item.setDescription("from server");
        item.setName("server pants");
        item.setUserUuid("121221");

        item = itemRepository.save(item);

        return ResponseEntity.ok(item);
    }
}
