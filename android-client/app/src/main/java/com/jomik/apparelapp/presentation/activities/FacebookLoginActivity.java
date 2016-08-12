package com.jomik.apparelapp.presentation.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jomik.apparelapp.R;
import com.jomik.apparelapp.infrastructure.providers.SqlOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class FacebookLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton)findViewById(R.id.fb_login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                try {
                                    // Get existing user if applicable
                                    SqlOpenHelper sqlOpenHelper = new SqlOpenHelper(getApplicationContext());
                                    Cursor cursor = sqlOpenHelper.getReadableDatabase()
                                            .query("users", new String[] {"_id"}, "facebook_id = ?", new String[] {object.getString("id")}, null, null, null);

                                    if(!cursor.moveToNext()) {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("uuid", UUID.randomUUID().toString());
                                        contentValues.put("facebook_id", object.getString("id"));
                                        contentValues.put("name", object.getString("name"));
                                        sqlOpenHelper.getWritableDatabase().insert("users", null, contentValues);
                                    }
                                    cursor.close();

                                    Toast.makeText(FacebookLoginActivity.this, "Welcome " + object.getString("name"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent i = new Intent(getActivity(), TabbedActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(FacebookLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private Activity getActivity() {
        return this;
    }
}
