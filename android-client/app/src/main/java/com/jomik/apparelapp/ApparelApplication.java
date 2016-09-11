package com.jomik.apparelapp;

/**
 * Created by Joe Deluca on 4/3/2016.
 */

import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

public class ApparelApplication extends Application {

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 30L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;


    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        ContentResolver.addPeriodicSync(
                AuthenticationManager.getSyncAccount(getApplicationContext()),
                ApparelContract.AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);

    }
}