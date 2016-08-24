package com.apparel.domain.model.photo;

import com.apparel.domain.model.ApparelEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
@Entity
public class Photo extends ApparelEntity {
    @Lob
    @Column(name="thumbnail", nullable=false, columnDefinition="mediumblob")
    private byte[] thumbnail;
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
}