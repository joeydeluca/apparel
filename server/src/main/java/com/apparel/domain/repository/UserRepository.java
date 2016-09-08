package com.apparel.domain.repository;

import com.apparel.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mick on 4/16/2016.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByFacebookId(String facebookId);
}
