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
import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.event.EventsRepository;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

import java.util.Date;

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
            setTitle("event a");
            setLocation("toronto");
            setStartDate(new Date());
            setEndDate(new Date());
            setOwner(AuthenticationManager.getAuthenticatedUser());
        }});

    }
}