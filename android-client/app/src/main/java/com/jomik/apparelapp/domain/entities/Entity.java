package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity implements Serializable {
    @DatabaseField(columnName="uuid", id = true)
    private String uuid;
    @DatabaseField(columnName="marked_for_delete")
    private boolean markedForDelete;
    @DatabaseField(columnName="version")
    private int version;

    public Entity() {
        version = 0;
        markedForDelete = false;
        uuid = UUID.randomUUID().toString();
    }

    @JsonIgnore
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        /*if(getId() != null) {
            values.put(ApparelContract.CommonColumns._ID, getId());
        }*/
        values.put(ApparelContract.CommonColumns.UUID, getUuid());
        values.put(ApparelContract.CommonColumns.VERSION, getUuid());
        values.put(ApparelContract.CommonColumns.MARKED_FOR_DELETE, isMarkedForDelete());
        values.putAll(getExtraContentValues());

        return values;
    }

    @JsonIgnore
    protected abstract ContentValues getExtraContentValues();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }

    public void incrementVersion() {
        version++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return !(uuid != null ? !uuid.equals(entity.uuid) : entity.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
