package com.jomik.apparelapp;

/**
 * Created by Joe Deluca on 4/3/2016.
 */

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;

public class ApparelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        // Initiate Ormlite
        new OrmLiteSqlHelper(getApplicationContext());
    }
}