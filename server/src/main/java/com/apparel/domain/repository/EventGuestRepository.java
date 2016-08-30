package com.apparel.domain.repository;

import com.apparel.domain.model.EventGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Joe Deluca on 8/29/2016.
 */
@Repository
public interface EventGuestRepository extends JpaRepository<EventGuest, String> {
    Set<EventGuest> findByGuestUuid(String guestUuid);
}

