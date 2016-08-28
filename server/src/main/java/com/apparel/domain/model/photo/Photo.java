package com.apparel.domain.model.photo;

import com.apparel.domain.model.ApparelEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
@Entity
public class Photo extends ApparelEntity {
    private String photoPath;
    private String photoPathSmall;

    @JsonIgnore
    @Lob
    @Column(name="thumbnail", nullable=false, columnDefinition="mediumblob")
    private byte[] thumbnail;

    @JsonIgnore
    @Lob
    @Column(name="size_a", nullable=false, columnDefinition="mediumblob")
    private byte[] sizeA;

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(final byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public byte[] getSizeA() {
        return sizeA;
    }

    public void setSizeA(final byte[] sizeA) {
        this.sizeA = sizeA;
    }

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
}
