package com.jomik.apparelapp.presentation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.jomik.apparelapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Class startClass = AccessToken.getCurrentAccessToken() == null ? FacebookLoginActivity.class : TabbedActivity.class;
        
        Intent intent = new Intent(this, startClass);
        startActivity(intent);
        finish();
    }
}
