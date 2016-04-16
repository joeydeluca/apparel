package com.jomik.apparel.interfaces;

import com.jomik.apparel.domain.model.user.User;
import com.jomik.apparel.domain.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by Mick on 4/16/2016.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(final IUserService userService){
        this.userService = userService;
    }


    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<User> findOne(@PathVariable("id") String id){
        final User user = userService.findOne(id);

        return ResponseEntity.ok(user);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Collection<User>> findAll(){
        final Collection<User> user = userService.findAll();

        return ResponseEntity.ok(user);
    }
}
