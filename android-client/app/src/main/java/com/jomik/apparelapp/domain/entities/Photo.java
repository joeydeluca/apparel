package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
public class Photo extends Entity {
    private String photoPath;
    private String photoPathSmall;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPathSmall() {
        return photoPathSmall;
    }

    public void setPhotoPathSmall(String photoPathSmall) {
        this.photoPathSmall = photoPathSmall;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Photos.LOCAL_PATH, getPhotoPath());
        values.put(ApparelContract.Photos.LOCAL_PATH_SM, getPhotoPathSmall());
        return values;
    }
}
