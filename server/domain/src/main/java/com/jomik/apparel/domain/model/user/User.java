package com.jomik.apparel.domain.model.user;

import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.photo.Photo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "user", catalog = "appareldb")
public class User extends ApparelEntity {
    private String username;

    private String email; //username maybe?

    private String password;

    @Column(name = "display_photo_id")
    private String displayPhotoId;

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

}
