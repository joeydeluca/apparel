package com.jomik.apparel.domain.model.item;


import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.photo.Photo;

import javax.persistence.*;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "item", catalog = "appareldb")
public class Item extends ApparelEntity {
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ItemColor itemColor;
    @Enumerated(EnumType.STRING)
    private ItemPattern itemPattern;
    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;
    @Transient
    private Photo photo;
}
