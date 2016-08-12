package com.jomik.apparelapp.infrastructure.services;

import android.content.Context;
import android.database.Cursor;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.infrastructure.providers.SqlOpenHelper;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class AuthenticationManager {

    private static User user;

    public static User getAuthenticatedUser(Context context) {
        if(user == null) {

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if(accessToken == null) {
                return null;
            }

            String facebookId = accessToken.getUserId();

            SqlOpenHelper sqlOpenHelper = new SqlOpenHelper(context);
            Cursor cursor = sqlOpenHelper.getReadableDatabase()
                            .query("users", new String[] {"_id", "uuid", "name"}, "facebook_id = ?", new String[] {facebookId}, null, null, null);

            if(cursor.moveToNext()) {
                user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                user.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setFacebookId(facebookId);
            }
            cursor.close();
        }

        return user;
    }

    public static void logout() {
        user = null;
        LoginManager.getInstance().logOut();
    }
}
