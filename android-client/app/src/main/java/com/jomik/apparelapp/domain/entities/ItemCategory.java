package com.jomik.apparelapp.domain.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Deluca on 3/24/2016.
 */
public enum ItemCategory {
    PLEASE_CHOOSE("Please Choose"),
    ACCESSORIES("Accessories"),
    BAGS("Bags"),
    DRESSES("Dresses"),
    JACKETS("Jackets"),
    PANTS("Pants"),
    SHORTS("Shorts"),
    SHOES("Shoes"),
    SKIRTS("Skirts"),
    SWEATERS("Sweaters"),
    TOPS("Tops"),
    OTHER("Other")
    ;

    private final String displayName;

    ItemCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ItemCategory getEnumFromDisplayName(String input) {
        for(ItemCategory itemCategory : ItemCategory.values()) {
            if(itemCategory.displayName.equals(input)) {
                return itemCategory;
            }
        }

        return null;
    }

    public static String[] getCategoryLabels() {
        List<String> labels = new ArrayList<>();
        for(ItemCategory itemCategory : ItemCategory.values()) {
            labels.add(itemCategory.getDisplayName());
        }

        return labels.toArray(new String[labels.size()]);
    }
}
