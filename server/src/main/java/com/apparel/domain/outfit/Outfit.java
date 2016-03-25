package main.java.com.apparel.domain.outfit;

import main.java.com.apparel.domain.Entity;
import main.java.com.apparel.domain.item.Item;

import java.util.List;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Outfit extends Entity {
    private String name;
    private String description;
    private List<Item> items;
}
