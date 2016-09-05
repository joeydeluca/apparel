package com.jomik.apparelapp.infrastructure.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.presentation.activities.FacebookLoginActivity;

import java.sql.SQLException;

/**
 * Created by Joe Deluca on 4/27/2016.
 */
public class AuthenticationManager {

    private static User user;

    public static User getAuthenticatedUser(Context context) {

        if(user == null) {

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null) {
                Intent intent = new Intent(context, FacebookLoginActivity.class);
                context.startActivity(intent);
            }

            OrmLiteSqlHelper helper  = new OrmLiteSqlHelper(context);
            try {
                user = helper.getUserDao().queryBuilder().where().eq("facebook_id", accessToken.getUserId()).queryForFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(user == null) {
            Intent intent = new Intent(context, FacebookLoginActivity.class);
            context.startActivity(intent);
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
