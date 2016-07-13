package com.jomik.apparelapp.infrastructure.services;

import android.content.Context;
import android.database.Cursor;

import com.jomik.apparelapp.domain.entities.user.User;
import com.jomik.apparelapp.infrastructure.providers.SqlOpenHelper;

import java.util.UUID;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class AuthenticationManager {

    private static User user;

    public static User getAuthenticatedUser(Context context) {
        if(user == null) {
            user = new User();
            SqlOpenHelper sqlOpenHelper = new SqlOpenHelper(context);
            Cursor cursor = sqlOpenHelper.getReadableDatabase()
                            .query("users", new String[] {"_id", "uuid"}, "_id=1", null, null, null, null);

            if(cursor.moveToNext()) {
                user.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                user.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
            }
            cursor.close();
        }

        return user;
    }
}
