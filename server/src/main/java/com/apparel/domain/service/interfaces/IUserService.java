package com.apparel.domain.service.interfaces;

import com.apparel.domain.model.User;

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
