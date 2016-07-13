package com.jomik.apparelapp.infrastructure.providers;

import android.provider.BaseColumns;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract.*;

import java.util.UUID;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/4/2016.
 */
public interface DbSchema {
    String DB_NAME = "apparel.db";

    String TBL_USERS = "users";
    String TBL_ITEMS = "items";
    String TBL_EVENTS = "events";
    String TBL_EVENT_GUESTS = "event_guests";

    String DDL_CREATE_TBL_ITEMS =
            "CREATE TABLE "+TBL_ITEMS+" (" +
                    Items._ID+"           INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Items.UUID+"          TEXT,\n" +
                    Items.NAME+"          TEXT,\n" +
                    Items.DESCRIPTION+"   TEXT,\n" +
                    Items.ITEM_COLOR+"    TEXT,\n" +
                    Items.ITEM_PATTERN+"  TEXT,\n" +
                    Items.ITEM_CATEGORY+" TEXT,\n" +
                    Items.PHOTO_ID+"      TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENTS =
            "CREATE TABLE "+TBL_EVENTS+" (" +
                    Events._ID+"          INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Events.UUID+"         TEXT,\n" +
                    Events.TITLE+"        TEXT,\n" +
                    Events.LOCATION+"     TEXT,\n" +
                    Events.START_DATE+"   TEXT,\n" +
                    Events.END_DATE+"     TEXT,\n" +
                    Events.OWNER_UUID +"  TEXT,\n" +
                    Events.PHOTO_ID+"     TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENT_GUESTS =
            "CREATE TABLE "+TBL_EVENT_GUESTS+" (" +
                    EventGuests._ID+"      INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    EventGuests.EVENT_ID+" INTEGER,\n" +
                    EventGuests.GUEST_ID+" INTEGER\n" +
                    ")";

    String DDL_CREATE_TBL_USERS =
            "CREATE TABLE "+TBL_USERS+" (" +
                    Users._ID+"     INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Users.UUID+"    TEXT\n" +
                    ")";

    String DDL_INSERT_USER_RECORD =
            "INSERT INTO "+TBL_USERS+"("+Users.UUID+") VALUES(?)";


    String DDL_DROP_TBL_ITEMS = "DROP TABLE IF EXISTS " + TBL_ITEMS;
    String DDL_DROP_TBL_EVENTS = "DROP TABLE IF EXISTS " + TBL_EVENTS;
    String DDL_DROP_TBL_EVENT_GUESTS = "DROP TABLE IF EXISTS " + TBL_EVENT_GUESTS;
    String DDL_DROP_TBL_USERS = "DROP TABLE IF EXISTS " + TBL_USERS;


    String EVENT_GUESTS_FROM_JOIN = TBL_EVENT_GUESTS + " " +
            "JOIN " + TBL_EVENTS + " ON("+TBL_EVENTS+"."+ Events._ID+" = "+TBL_EVENT_GUESTS+"."+EventGuests.EVENT_ID+") " +
            "JOIN " + TBL_EVENTS + " ON("+TBL_EVENTS+"."+ Events._ID+" = "+TBL_EVENT_GUESTS+"."+EventGuests.EVENT_ID+")";




}
