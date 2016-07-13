package com.jomik.apparelapp.domain.entities;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public abstract class Entity {
    private Long id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return !(id != null ? !id.equals(entity.id) : entity.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
