package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@DatabaseTable(tableName="users")
public class User extends Entity {
    @DatabaseField(columnName="name")
    private String name;
    @DatabaseField(columnName="facebook_id")
    private String facebookId;

    public User() {}

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
