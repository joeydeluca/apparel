package com.jomik.apparel.domain.model.photo;

import com.jomik.apparel.domain.model.ApparelEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by Joe Deluca on 3/23/2016.
 */
@Entity
@Table(name = "photo", catalog = "appareldb")
public class Photo extends ApparelEntity {
    @Lob
    @Column(name="thumbnail", nullable=false, columnDefinition="mediumblob")
    private byte[] thumbnail;
    @Lob
    @Column(name="size_a", nullable=false, columnDefinition="mediumblob")
    private byte[] sizeA;
}
