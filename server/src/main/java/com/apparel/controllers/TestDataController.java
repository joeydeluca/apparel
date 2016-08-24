package com.apparel.controllers;

import com.jomik.apparel.domain.model.user.User;
import com.jomik.apparel.domain.service.interfaces.IUserService;
import com.jomik.apparel.interfaces.users.AbstractUserController;
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
public class TestDataController extends AbstractUserController{

    private final IUserService userService;

    @Autowired
    public TestDataController(final IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
            value = "/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<String> populateTestData(){
        User testUser = new User();
        testUser.setEmail("mick@mickiscool.com");
        testUser.setPassword("coolpasswordmick");
        testUser.setUsername("coolmick");
        testUser.setId(UUID.randomUUID().toString());

        userService.create(testUser);

        return ResponseEntity.ok("ok");
    }
}
