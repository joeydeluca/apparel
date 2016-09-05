package com.apparel.domain.repository;

import com.apparel.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Created by Joe Deluca on 8/28/2016.
 */
public interface EventRepository extends JpaRepository<Event, String> {

    Set<Event> findByOwnerUuid(String uuid);

    Set<Event> findByEventGuestsGuestUuid(String uuid);

}
