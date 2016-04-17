package com.jomik.apparel.interfaces.users;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mick on 4/17/2016.
 */
@RestController
public class WardrobeController extends AbstractUserController {
    @RequestMapping(
            value = "/{id}/wardrobe",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<String> wardrobeEndpoing(@PathVariable("id") String userId){
        return ResponseEntity.ok("User id:" + userId);
    }
}
