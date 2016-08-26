package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
public class EventGuestOutfitItem extends Entity {
    private String eventGuestOutfitUuid;
    private String itemUuid;

    public String getEventGuestOutfitUuid() {
        return eventGuestOutfitUuid;
    }

    public void setEventGuestOutfitUuid(String eventGuestOutfitUuid) {
        this.eventGuestOutfitUuid = eventGuestOutfitUuid;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.EventGuestOutfitItems.EVENT_GUEST_OUTFIT_UUID, getEventGuestOutfitUuid());
        values.put(ApparelContract.EventGuestOutfitItems.ITEM_UUID, getItemUuid());
        return values;
    }
}
