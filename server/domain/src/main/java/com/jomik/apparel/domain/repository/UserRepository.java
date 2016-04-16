package com.jomik.apparel.domain.repository;

import com.jomik.apparel.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mick on 4/16/2016.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
