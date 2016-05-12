package com.jomik.apparelapp.domain.repositories.usereventoutfit;

import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;
import com.jomik.apparelapp.domain.repositories.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Deluca on 5/11/2016.
 */
public class UserEventOutfitInMemoryRepository extends InMemoryRepository<UserEventOutfit> {
    private static UserEventOutfitInMemoryRepository instance;
    private final List<Event> events = new ArrayList<>();

    private UserEventOutfitInMemoryRepository() {

    }

    public static UserEventOutfitInMemoryRepository getInstance() {
        if(instance == null) {
            instance = new UserEventOutfitInMemoryRepository();
        }
        return instance;
    }
}
