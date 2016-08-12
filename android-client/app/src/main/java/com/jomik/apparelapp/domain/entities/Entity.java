package com.jomik.apparelapp.domain.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public abstract class Entity implements Serializable {
    private Long id;
    private String uuid;
    private Date createdDate;

    public Entity() {
        createdDate = new Date();
    }

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
