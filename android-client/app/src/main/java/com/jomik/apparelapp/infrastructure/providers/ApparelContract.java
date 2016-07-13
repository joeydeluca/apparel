package com.jomik.apparelapp.infrastructure.providers;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/3/2016.
 */
public class ApparelContract {

    public static final String AUTHORITY = "com.jomik.apparelapp.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Events implements CommonColumns {
        public static String TITLE = "title";
        public static String LOCATION = "location";
        public static String START_DATE = "start_date";
        public static String END_DATE = "end_date";
        public static String OWNER_UUID = "owner_uuid";
        public static String PHOTO_ID = "photo_id";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "events");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.events";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.events";

        public static final String[] PROJECTION_ALL =  {_ID, TITLE, LOCATION, START_DATE, END_DATE, OWNER_UUID, PHOTO_ID};
        public static final String SORT_ORDER_DEFAULT = TITLE + " ASC";
    }

    public static final class EventGuests implements CommonColumns {
        public static String EVENT_ID = "event_id";
        public static String GUEST_ID = "guest_id";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "event_guests");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.event_guests";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.event_guests";
    }

    public static final class Users implements CommonColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "users");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.users";
        public static final String CONTENT_EVENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.users";
    }

    public static final class Items implements CommonColumns {
        public static String NAME = "name";
        public static String DESCRIPTION = "description";
        public static String ITEM_COLOR = "item_color";
        public static String ITEM_PATTERN = "item_pattern";
        public static String ITEM_CATEGORY = "item_category";
        public static String PHOTO_ID = "photo_id";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(ApparelContract.CONTENT_URI, "items");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.jomik.apparel.items";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.jomik.apparel.items";

        public static final String[] PROJECTION_ALL =
                {_ID, UUID, NAME, DESCRIPTION, ITEM_COLOR, ITEM_PATTERN, ITEM_CATEGORY, PHOTO_ID};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }

    public static final class UserEventOutfits implements CommonColumns { }

    public static interface CommonColumns extends BaseColumns {
        public static String UUID = "uuid";
    }

}
