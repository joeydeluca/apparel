package com.jomik.apparelapp.domain.entities.user;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.photo.Photo;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class User extends Entity {
    private String name;
    private Photo photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
