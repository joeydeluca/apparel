package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public abstract class Entity implements Serializable {
    private Long id;
    private String uuid;
    boolean markedForDelete;
    private Integer version;

    public Entity() {
        version = 0;
        markedForDelete = false;
        uuid = UUID.randomUUID().toString();
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if(getId() != null) {
            values.put(ApparelContract.CommonColumns._ID, getId());
        }
        values.put(ApparelContract.CommonColumns.UUID, getUuid());
        values.put(ApparelContract.CommonColumns.VERSION, getUuid());
        values.put(ApparelContract.CommonColumns.MARKED_FOR_DELETE, isMarkedForDelete());
        values.putAll(getExtraContentValues());

        return values;
    }

    protected abstract ContentValues getExtraContentValues();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
