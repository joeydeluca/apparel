package com.apparel.domain.repository;

import com.apparel.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Mick on 4/16/2016.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
