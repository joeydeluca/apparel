package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
@DatabaseTable(tableName="photos")
public class Photo extends Entity {
    @DatabaseField(columnName="local_path")
    private String photoPath;
    @DatabaseField(columnName="local_path_sm")
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

    @JsonIgnore
    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.Photos.LOCAL_PATH, getPhotoPath());
        values.put(ApparelContract.Photos.LOCAL_PATH_SM, getPhotoPathSmall());
        return values;
    }
}
