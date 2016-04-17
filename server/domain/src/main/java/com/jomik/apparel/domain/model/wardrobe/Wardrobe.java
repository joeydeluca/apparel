package com.jomik.apparel.domain.model.wardrobe;

import com.jomik.apparel.domain.model.ApparelEntity;
import com.jomik.apparel.domain.model.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Do we need this? The wardrobe is just the items for a user.
 *
 * Created by Joe Deluca on 3/22/2016.
 */
@Entity
@Table(name = "wardrobe", catalog = "appareldb")
public class Wardrobe extends ApparelEntity {

    private String userId;

    @OneToMany
    @JoinTable(name = "wardrobe_item", joinColumns = @JoinColumn(name = "id"))
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(final List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
