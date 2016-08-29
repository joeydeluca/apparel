package com.apparel.controllers.dtos;

import com.apparel.domain.model.ApparelEntity;
import com.apparel.domain.model.item.Item;

/**
 * Created by Joe Deluca on 8/28/2016.
 */
public class ItemDto extends EntityDto {
    private String name;
    private String description;
    private String itemCategory;
    private String userUuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Item toEntity() {
        Item item = new Item();
        super.toEntity(item);
        item.setName(name);
        item.setDescription(description);
        item.setItemCategory(itemCategory);
        item.setUserUuid(userUuid);
        return item;
    }
}
