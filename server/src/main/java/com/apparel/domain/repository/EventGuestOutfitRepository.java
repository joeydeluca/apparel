package com.apparel.domain.repository;

import com.apparel.domain.model.EventGuest;
import com.apparel.domain.model.EventGuestOutfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/29/2016.
 */
@Repository
public interface EventGuestOutfitRepository extends JpaRepository<EventGuestOutfit, String> {
    Set<EventGuestOutfit> findByEventGuestUuidIn(List<String> EventGuestUuids);
    Set<EventGuestOutfit> findByEventGuestEventUuid(String eventUuid);
}

