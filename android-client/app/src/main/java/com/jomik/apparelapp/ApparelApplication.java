package com.jomik.apparelapp;

/**
 * Created by Joe Deluca on 4/3/2016.
 */

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jomik.apparelapp.domain.entities.item.Item;
import com.jomik.apparelapp.domain.entities.item.ItemCategory;
import com.jomik.apparelapp.domain.entities.item.ItemColor;
import com.jomik.apparelapp.domain.entities.item.ItemPattern;
import com.jomik.apparelapp.domain.repositories.RepositoryFactory;
import com.jomik.apparelapp.domain.repositories.item.ItemsRepository;

public class ApparelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        createTestData();
    }

    private void createTestData() {
        ItemsRepository repository = RepositoryFactory.getItemsRepository(RepositoryFactory.Type.IN_MEMORY);

        for(int i = 0; i < 10; i++) {
            repository.save(new Item() {{
                setName("pants");
                setDescription("this is a description");
                setItemCategory(ItemCategory.BOTTOMS);
                setItemColor(ItemColor.GRAY);
                setItemPattern(ItemPattern.PLAIN);
                setPhotoId(123);
            }});
        }

    }
}