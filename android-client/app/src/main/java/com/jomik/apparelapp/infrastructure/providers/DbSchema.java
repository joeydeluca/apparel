package com.jomik.apparelapp.infrastructure.providers;

import android.provider.BaseColumns;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.*;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/4/2016.
 */
public interface DbSchema {
    String DB_NAME = "apparel.db";

    String TBL_ITEMS = "items";
    String TBL_EVENTS = "events";

    String DDL_CREATE_TBL_ITEMS =
            "CREATE TABLE "+TBL_ITEMS+" (" +
                    BaseColumns._ID+"     INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Items.NAME+"          TEXT,\n" +
                    Items.DESCRIPTION+"   TEXT,\n" +
                    Items.ITEM_COLOR+"    TEXT,\n" +
                    Items.ITEM_PATTERN+"  TEXT,\n" +
                    Items.ITEM_CATEGORY+" TEXT,\n" +
                    Items.PHOTO_ID+"      TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENTS =
            "CREATE TABLE "+TBL_EVENTS+" (" +
                    BaseColumns._ID+"     INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Events.TITLE+"        TEXT,\n" +
                    Events.LOCATION+"     TEXT,\n" +
                    Events.START_DATE+"   TEXT,\n" +
                    Events.END_DATE+"     TEXT,\n" +
                    Events.OWNER+"        TEXT,\n" +
                    Events.PHOTO_ID+"     TEXT\n" +
                    ")";

    String DDL_DROP_TBL_ITEMS = "DROP TABLE IF EXISTS " + TBL_ITEMS;
    String DDL_DROP_TBL_EVENTS = "DROP TABLE IF EXISTS " + TBL_EVENTS;

}
