package com.apparel.domain.model;

import com.apparel.domain.model.ApparelEntity;

import javax.persistence.Entity;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class User extends ApparelEntity {
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
}
