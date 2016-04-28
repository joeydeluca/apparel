package com.jomik.apparelapp.infrastructure.services;

import com.jomik.apparelapp.domain.entities.user.User;

import java.util.UUID;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class AuthenticationManager {

    private static User user;

    public static User getAuthenticatedUser() {
        if(user == null) {
            user = new User();
            user.setId(UUID.randomUUID().toString());
        }

        return user;
    }
}
