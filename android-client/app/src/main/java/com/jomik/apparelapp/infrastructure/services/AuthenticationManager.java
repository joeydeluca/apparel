package com.jomik.apparelapp.infrastructure.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
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

    public static Account getSyncAccount(Context context) {
        return createDummyAccount(context);
    }

    public static void logout() {
        user = null;
        LoginManager.getInstance().logOut();
    }

    private static Account createDummyAccount(Context context) {
        Account dummyAccount = new Account("ApparelApp", "com.apparel");
        AccountManager accountManager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(dummyAccount, null, null);
        ContentResolver.setSyncAutomatically(dummyAccount, ApparelContract.AUTHORITY, true);
        return dummyAccount;
    }

}
