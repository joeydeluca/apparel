package com.jomik.apparelapp.infrastructure.providers;

import com.jomik.apparelapp.infrastructure.providers.ApparelContract.*;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/4/2016.
 */
public interface DbSchema {
    String DB_NAME = "apparel.db";

    String TBL_PHOTOS = "photos";
    String TBL_USERS = "users";
    String TBL_ITEMS = "items";
    String TBL_EVENTS = "events";
    String TBL_EVENT_GUESTS = "event_guests";
    String TBL_EVENT_GUEST_OUTFITS = "event_guest_outfits";
    String TBL_EVENT_GUEST_OUTFIT_ITEMS = "event_guest_outfit_items";

    String PREFIX_TBL_PHOTOS = "p";
    String PREFIX_TBL_USERS = "u";
    String PREFIX_TBL_ITEMS = "i";
    String PREFIX_TBL_EVENTS = "e";
    String PREFIX_TBL_EVENT_GUESTS = "eg";
    String PREFIX_TBL_EVENT_GUEST_OUTFITS = "ego";
    String PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS = "egoi";

    String DDL_CREATE_TBL_PHOTOS =
            "CREATE TABLE "+TBL_PHOTOS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    Photos.LOCAL_PATH+"    TEXT,\n" +
                    Photos.LOCAL_PATH_SM+" TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_ITEMS =
            "CREATE TABLE "+TBL_ITEMS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    Items.NAME+"          TEXT,\n" +
                    Items.DESCRIPTION+"   TEXT,\n" +
                    Items.PHOTO_UUID+"    TEXT,\n" +
                    Items.USER_UUID+"     TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENTS =
            "CREATE TABLE "+TBL_EVENTS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    Events.TITLE+"          TEXT,\n" +
                    Events.LOCATION+"       TEXT,\n" +
                    Events.START_DATE+"     TEXT,\n" +
                    Events.END_DATE+"       TEXT,\n" +
                    Events.DESCRIPTION+"    TEXT,\n" +
                    Events.OWNER_UUID +"    TEXT,\n" +
                    Events.PHOTO_UUID +"    TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENT_GUESTS =
            "CREATE TABLE "+TBL_EVENT_GUESTS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    EventGuests.EVENT_UUID+"        TEXT,\n" +
                    EventGuests.GUEST_UUID+"        TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENT_GUEST_OUTFITS =
            "CREATE TABLE "+TBL_EVENT_GUEST_OUTFITS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    EventGuestOutfits.DESCRIPTION+" TEXT,\n" +
                    EventGuestOutfits.EVENT_DATE+"  TEXT,\n" +
                    EventGuestOutfits.EVENT_GUEST_UUID+"  TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_EVENT_GUEST_OUTFIT_ITEMS =
            "CREATE TABLE "+TBL_EVENT_GUEST_OUTFIT_ITEMS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    EventGuestOutfitItems.EVENT_GUEST_OUTFIT_UUID+" TEXT,\n" +
                    EventGuestOutfitItems.ITEM_UUID+"               TEXT\n" +
                    ")";

    String DDL_CREATE_TBL_USERS =
            "CREATE TABLE "+TBL_USERS+" (" +
                    CommonColumns._ID+"    INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    CommonColumns.UUID+"   TEXT,\n" +
                    CommonColumns.MARKED_FOR_DELETE+" INTEGER,\n" +
                    Users.NAME+"       TEXT,\n" +
                    Users.FACEBOOK_ID+" TEXT\n" +
                    ")";

    String DDL_DROP_TBL_EVENT_GUEST_OUTFIT_ITEMS = "DROP TABLE IF EXISTS " + TBL_EVENT_GUEST_OUTFIT_ITEMS;
    String DDL_DROP_TBL_EVENT_GUEST_OUTFITS = "DROP TABLE IF EXISTS " + TBL_EVENT_GUEST_OUTFITS;
    String DDL_DROP_TBL_EVENT_GUESTS = "DROP TABLE IF EXISTS " + TBL_EVENT_GUESTS;
    String DDL_DROP_TBL_EVENTS = "DROP TABLE IF EXISTS " + TBL_EVENTS;
    String DDL_DROP_TBL_ITEMS = "DROP TABLE IF EXISTS " + TBL_ITEMS;
    String DDL_DROP_TBL_USERS = "DROP TABLE IF EXISTS " + TBL_USERS;
    String DDL_DROP_TBL_PHOTOS = "DROP TABLE IF EXISTS " + TBL_PHOTOS;


    String EVENT_GUESTS_FROM_JOIN = TBL_EVENT_GUESTS + " " +
            "JOIN " + TBL_EVENTS + " ON("+TBL_EVENTS+"."+ Events._ID+" = "+TBL_EVENT_GUESTS+"."+EventGuests.EVENT_UUID +") " +
            "JOIN " + TBL_EVENTS + " ON("+TBL_EVENTS+"."+ Events._ID+" = "+TBL_EVENT_GUESTS+"."+EventGuests.EVENT_UUID +")";

    String FROM_ITEMS =
            TBL_ITEMS + " " + PREFIX_TBL_ITEMS + " " +
            "LEFT JOIN "+TBL_PHOTOS+" "+PREFIX_TBL_PHOTOS+" on "+PREFIX_TBL_PHOTOS+".uuid = "+ PREFIX_TBL_ITEMS +".photo_uuid";

    String FROM_EVENTS =
            TBL_EVENTS + " " + PREFIX_TBL_EVENTS + " " +
                    "LEFT JOIN "+TBL_PHOTOS+" "+PREFIX_TBL_PHOTOS+" on "+PREFIX_TBL_PHOTOS+".uuid = "+ PREFIX_TBL_EVENTS +".photo_uuid " +
                    "LEFT JOIN "+TBL_USERS+" "+PREFIX_TBL_USERS+" on "+PREFIX_TBL_USERS+".uuid = "+ PREFIX_TBL_EVENTS +".owner_uuid";

    String FROM_EVENT_GUESTS =
                    TBL_EVENT_GUESTS + " " + PREFIX_TBL_EVENT_GUESTS + " " +
                   "JOIN "+TBL_EVENTS+" "+PREFIX_TBL_EVENTS+" on "+PREFIX_TBL_EVENTS+".uuid = "+ PREFIX_TBL_EVENT_GUESTS +".event_uuid " +
                   "LEFT JOIN "+TBL_USERS+" "+PREFIX_TBL_USERS+" on "+PREFIX_TBL_USERS+".uuid = "+ PREFIX_TBL_EVENT_GUESTS +".guest_uuid " +
                   "LEFT JOIN "+TBL_EVENT_GUEST_OUTFITS+" "+PREFIX_TBL_EVENT_GUEST_OUTFITS+" on "+PREFIX_TBL_EVENT_GUEST_OUTFITS+".event_guest_uuid = "+ PREFIX_TBL_EVENT_GUESTS +".uuid and "+PREFIX_TBL_EVENT_GUEST_OUTFITS+".event_date = ?" +
                   "LEFT JOIN "+TBL_EVENT_GUEST_OUTFIT_ITEMS+" "+PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS+" on "+PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS+".event_guest_outfit_uuid = "+PREFIX_TBL_EVENT_GUEST_OUTFITS+".uuid " +
                   "LEFT JOIN "+TBL_ITEMS+" "+PREFIX_TBL_ITEMS+" on "+PREFIX_TBL_ITEMS+".uuid = "+PREFIX_TBL_EVENT_GUEST_OUTFIT_ITEMS+".item_uuid " +
                   "LEFT JOIN "+TBL_PHOTOS+" "+PREFIX_TBL_PHOTOS+" on "+PREFIX_TBL_PHOTOS+".uuid = "+PREFIX_TBL_ITEMS+".photo_uuid"

    ;

    String FROM_EVENT_GUEST_OUTFIT_ITEMS = TBL_EVENT_GUEST_OUTFIT_ITEMS;
}
