package com.jomik.apparel.domain.service;

import com.jomik.apparel.domain.model.user.User;
import com.jomik.apparel.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

/**
 * Created by Mick on 4/16/2016.
 */
@Service
@Transactional
public class UserService implements IUserService{

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User findOne(final String id){
        final User user = userRepository.findOne(id);
        if(user == null){
            throw new NoResultException("User does not exist");
        }
        return user;
    }

    @Override
    public User create(User testUser) {
        return userRepository.save(testUser);
    }
}
