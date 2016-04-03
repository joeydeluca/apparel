package com.jomik.apparelapp;

/**
 * Created by Joe Deluca on 4/3/2016.
 */

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class ApparelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}