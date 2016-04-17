package com.jomik.apparel.interfaces.users;

import com.jomik.apparel.domain.model.item.Item;
import com.jomik.apparel.domain.model.wardrobe.Wardrobe;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Mick on 4/17/2016.
 */
@RestController
public class WardrobeController extends AbstractUserController {

    @RequestMapping(
            value = "/{id}/wardrobe",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Wardrobe> getWardrobe(@PathVariable("id") String userId){
        return null;
    }

    /**
     * Create a wardrobe for the specified user.
     *
     * @param wardrobe
     * @return
     */
    @RequestMapping(
            value = "/{id}/wardrobe",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Wardrobe> createWardrobe(@PathVariable("id") String userId, @RequestBody Wardrobe wardrobe){
        return null;
    }

    /**
     * Create the passed in items and 'puts' them in the specified users wardrobe.
     *
     * @param userId
     * @param items
     * @return
     */
    @RequestMapping(
            value = "/{id}/wardrobe/items",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Wardrobe> createAndAddItemToWardrobe(@PathVariable("id") String userId, @RequestBody List<Item> items){
        return null;
    }
}
