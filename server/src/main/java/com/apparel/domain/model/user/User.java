package com.apparel.domain.model.user;

import com.apparel.domain.model.ApparelEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
public class User extends ApparelEntity {
    private String username;

    private String email; //username maybe?

    private String password;

    //todo profilePhotoId
    @Column(name = "profile_photo_id")
    private String profilePhotoId;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getProfilePhotoId() {
        return profilePhotoId;
    }

    public void setProfilePhotoId(String profilePhotoId) {
        this.profilePhotoId = profilePhotoId;
    }
}
