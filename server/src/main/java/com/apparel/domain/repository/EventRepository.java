package com.apparel.domain.repository;

import com.apparel.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Joe Deluca on 8/28/2016.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor {

    Set<Event> findByOwnerUuid(String uuid);

    Set<Event> findByEventGuestsGuestUuid(String uuid);

}
