package com.jomik.apparelapp.infrastructure.providers;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.jomik.apparelapp.domain.entities.event.Event;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/3/2016.
 */
public class ApparelContract {

    public static final String AUTHORITY = "com.jomik.apparelapp.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Photos implements CommonColumns {
        public static String UUID = "uuid";
        public static String LOCAL_PATH = "local_path";
        public static String LOCAL_PATH_SM = "local_path_sm";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "photos");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.photos";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.photos";
    }

    public static final class Events implements CommonColumns {
        public static String UUID = "uuid";
        public static String TITLE = "title";
        public static String LOCATION = "location";
        public static String START_DATE = "start_date";
        public static String END_DATE = "end_date";
        public static String DESCRIPTION = "description";
        public static String OWNER_UUID = "owner_uuid";
        public static String PHOTO_UUID = "photo_uuid";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "events");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.events";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.events";

        public static final String[] PROJECTION_ALL = new String[] {
                SqlHelper.getSelectColumn(Events._ID, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.UUID, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.TITLE, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.LOCATION, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.START_DATE, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.END_DATE, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.DESCRIPTION, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Events.OWNER_UUID, DbSchema.PREFIX_TBL_EVENTS),
                SqlHelper.getSelectColumn(Photos._ID, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Users.NAME, DbSchema.PREFIX_TBL_USERS),
                SqlHelper.getSelectColumn(Users.FACEBOOK_ID, DbSchema.PREFIX_TBL_USERS),
        };

        public static final String SORT_ORDER_DEFAULT = TITLE + " ASC";
    }

    public static final class Users implements CommonColumns {
        public static String UUID = "uuid";
        public static String NAME = "name";
        public static String FACEBOOK_ID = "facebook_id";
    }

    public static final class Items implements CommonColumns {
        public static String UUID = "uuid";
        public static String NAME = "name";
        public static String DESCRIPTION = "description";
        public static String ITEM_COLOR = "item_color";
        public static String ITEM_PATTERN = "item_pattern";
        public static String ITEM_CATEGORY = "item_category";
        public static String PHOTO_UUID = "photo_uuid";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "items");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.items";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.items";

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";

        public static final String[] PROJECTION_ALL = new String[] {
                DbSchema.PREFIX_TBL_ITEMS + "." + Items._ID, // needed for cursor adapter
                SqlHelper.getSelectColumn(Items._ID, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.UUID, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.NAME, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.DESCRIPTION, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.ITEM_COLOR, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.ITEM_PATTERN, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Items.ITEM_CATEGORY, DbSchema.PREFIX_TBL_ITEMS),
                SqlHelper.getSelectColumn(Photos._ID, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS),
                SqlHelper.getSelectColumn(Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS),
        };
    }

    public static final class EventGuests implements CommonColumns {
        public static String UUID = "uuid";
        public static String EVENT_UUID = "event_uuid";
        public static String GUEST_UUID = "guest_uuid";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "event_guests");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.event_guests";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.event_guests";
    }

    public static final class EventGuestOutfits implements CommonColumns {
        public static String UUID = "uuid";
        public static String DESCRIPTION = "description";
        public static String PHOTO_UUID = "photo_uuid";
        public static String EVENT_DATE = "event_date";
        public static String EVENT_GUEST_UUID = "event_guest_uuid";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "event_guest_outfits");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.event_guest_outfits";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.event_guest_outfits";

        public static final String[] PROJECTION_ALL =
                {_ID, UUID, DESCRIPTION, PHOTO_UUID, EVENT_DATE, EVENT_GUEST_UUID};
    }

    public static final class EventGuestOutfitItems implements CommonColumns {
        public static String UUID = "uuid";
        public static String EVENT_GUEST_OUTFIT_UUID = "event_guest_outfit_uuid";
        public static String ITEM_UUID = "item_uuid";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "event_guest_outfit_items");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.event_guest_outfit_items";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.event_guest_outfit_items";
    }

    public static interface CommonColumns extends BaseColumns {

    }

}