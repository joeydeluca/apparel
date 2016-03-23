package main.java.com.apparel.domain.item;

import main.java.com.apparel.domain.Entity;
import main.java.com.apparel.domain.photo.Photo;

/**
 * Created by Joe Deluca on 3/22/2016.
 */
public class Item extends Entity {
    private String name;
    private String description;
    private Colour colour;
    private Pattern pattern;
    private Photo photo;
}
