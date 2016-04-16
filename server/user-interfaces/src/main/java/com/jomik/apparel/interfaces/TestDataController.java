package com.jomik.apparel.interfaces;

import com.jomik.apparel.domain.model.user.User;
import com.jomik.apparel.domain.service.IUserService;
import com.jomik.apparel.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Mick on 4/16/2016.
 */
@RestController
@RequestMapping("/populateTestData")
public class TestDataController {

    private final IUserService userService;

    @Autowired
    public TestDataController(final IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
            value = "",
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
