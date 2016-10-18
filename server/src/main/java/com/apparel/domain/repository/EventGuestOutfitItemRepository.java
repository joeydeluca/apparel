package com.apparel.domain.repository;

import com.apparel.domain.model.EventGuestOutfitItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Joe Deluca on 8/29/2016.
 */
@Repository
public interface EventGuestOutfitItemRepository extends JpaRepository<EventGuestOutfitItem, String> {
    Set<EventGuestOutfitItem> findByEventGuestOutfitUuidIn(List<String> EventGuestOutfitUuids);

    @Transactional
    void deleteByEventGuestOutfitUuidIn(List<String> EventGuestOutfitUuids);

}

