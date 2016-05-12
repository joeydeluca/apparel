package com.jomik.apparelapp;

/**
 * Created by Joe Deluca on 4/3/2016.
 */

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jomik.apparelapp.domain.entities.event.Event;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.item.ItemCategory;
import com.jomik.apparelapp.domain.entities.item.ItemColor;
import com.jomik.apparelapp.domain.entities.item.ItemPattern;
import com.jomik.apparelapp.domain.entities.outfit.Outfit;
import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.domain.entities.usereventoutfit.UserEventOutfit;
import com.jomik.apparelapp.domain.repositories.ApparelRepository;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApparelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        createTestData();
    }

    private void createTestData() {
        ItemsRepository itemsRepository = RepositoryFactory.getItemsRepository(RepositoryFactory.Type.IN_MEMORY);
        EventsRepository eventsRepository = RepositoryFactory.getEventsRepository(RepositoryFactory.Type.IN_MEMORY);
        ApparelRepository userEventOutfitRepository = RepositoryFactory.getUserEventOutfitRepository(RepositoryFactory.Type.IN_MEMORY);

        User mike = new User();
        mike.setName("mike");
        User martha = new User();
        mike.setName("martha");
        User bob = new User();
        mike.setName("bob");

        for(int i = 0; i < 10; i++) {
            final int index = i;

            itemsRepository.save(new Item() {{
                setName("pants " + index);
                setDescription("this is a description");
                setItemCategory(ItemCategory.BOTTOMS);
                setItemColor(ItemColor.GRAY);
                setItemPattern(ItemPattern.PLAIN);
                setPhotoId(123);
            }});

            eventsRepository.save(new Event() {{
                setTitle("event " + index);
                setLocation("toronto");
                setStartDate(new Date());
                setEndDate(new Date());
                setOwner(new User());
            }});
        }

        eventsRepository.save(new Event() {{
            setTitle("Leonardo Worldwide Corporation");
            setLocation("111 Peter St");
            setStartDate(new Date());
            setEndDate(new Date());
            setOwner(AuthenticationManager.getAuthenticatedUser());
        }});


        for(Event event : eventsRepository.findAll()) {
            Outfit outfit = new Outfit();
            outfit.setDescription("this is my cool stuff");
            outfit.setItems(new ArrayList<Item>());
            outfit.getItems().add(new Item());
            outfit.getItems().add(new Item());
            outfit.getItems().add(new Item());

            event.setAttendees(new ArrayList<UserEventOutfit>());

            addUserToEvent(event, mike, outfit, userEventOutfitRepository);
            addUserToEvent(event, martha, outfit, userEventOutfitRepository);
            addUserToEvent(event, bob, outfit, userEventOutfitRepository);
            eventsRepository.save(event);
        }
    }

    private void addUserToEvent(Event event, User user, Outfit outfit, ApparelRepository userEventOutfitRepository) {
        UserEventOutfit userEventOutfit = new UserEventOutfit();
        userEventOutfit.setEvent(event);
        userEventOutfit.setUser(user);
        userEventOutfit.setOutfit(outfit);

        event.getAttendees().add(userEventOutfit);

        userEventOutfitRepository.save(userEventOutfit);
    }
}