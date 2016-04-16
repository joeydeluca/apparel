package com.jomik.apparel.domain.model.outfit;

import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.item.Item;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "outfit", catalog = "appareldb")
public class Outfit extends ApparelEntity {
    private String name;
    private String description;
    @ElementCollection
    @CollectionTable(name = "outfit_item", joinColumns = @JoinColumn(name = "id"))
    private List<Item> items;
}
