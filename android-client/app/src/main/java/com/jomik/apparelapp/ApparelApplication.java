package com.jomik.apparelapp;

/**
 * Created by Joe Deluca on 4/3/2016.
 */

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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

public class ApparelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }
}