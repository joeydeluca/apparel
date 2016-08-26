package com.jomik.apparelapp.domain.entities;

import android.content.ContentValues;

import com.jomik.apparelapp.infrastructure.providers.ApparelContract;

/**
 * Created by Joe Deluca on 8/26/2016.
 */
public class EventGuest extends Entity {
    private String eventUuid;
    private String guestUuid;

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    public String getGuestUuid() {
        return guestUuid;
    }

    public void setGuestUuid(String guestUuid) {
        this.guestUuid = guestUuid;
    }

    @Override
    protected ContentValues getExtraContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApparelContract.EventGuestOutfitItems.EVENT_GUEST_OUTFIT_UUID, getEventUuid());
        values.put(ApparelContract.EventGuestOutfitItems.ITEM_UUID, getGuestUuid());
        return values;
    }
}
