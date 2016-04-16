package com.jomik.apparel.domain.service;

import com.jomik.apparel.domain.model.user.User;

import java.util.Collection;

/**
 * Created by Mick on 4/16/2016.
 */
public interface IUserService {
    User create(User testUser);

    User findOne(String id);

    Collection<User> findAll();

    User update(User testUser);
}
