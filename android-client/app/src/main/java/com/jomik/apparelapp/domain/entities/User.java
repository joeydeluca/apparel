package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class User extends Entity {
    private String name;
    private String facebookId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Users.NAME, getName());
        values.put(ApparelContract.Users.FACEBOOK_ID, getFacebookId());
        return values;
    }
}
