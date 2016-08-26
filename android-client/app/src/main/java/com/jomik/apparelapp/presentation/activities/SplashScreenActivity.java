package com.jomik.apparelapp.presentation.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Class startClass;
        if(AccessToken.getCurrentAccessToken() == null) {
           startClass = FacebookLoginActivity.class;
        } else {
            startClass = TabbedActivity.class;


        }

        Intent intent = new Intent(this, startClass);
        startActivity(intent);
        finish();



    }
}
