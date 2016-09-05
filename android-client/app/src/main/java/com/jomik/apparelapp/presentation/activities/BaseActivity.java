package com.jomik.apparelapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;

/**
 * Created by Joe Deluca on 9/3/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(this, FacebookLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
