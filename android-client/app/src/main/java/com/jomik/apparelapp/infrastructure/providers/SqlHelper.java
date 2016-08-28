package com.jomik.apparelapp.infrastructure.providers;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.os.RemoteException;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.ItemCategory;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.domain.entities.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe Deluca on 8/4/2016.
 */
public class SqlHelper {

    public static DateFormat dateFormatForDisplay = new SimpleDateFormat("MMM dd, yyyy");
    public static DateFormat dateFormatForDb = new SimpleDateFormat("dd-MM-yyyy");

    public static String getSelectColumn(String columnName) {
        return getSelectColumn(columnName, null);
    }

    public static String getSelectColumn(String columnName, String prefix) {
        return prefix == null ? columnName :  prefix + "." + columnName + " as '" + prefix + "_" + columnName + "'";
    }

    public static String getString(Cursor cursor, String columnName, String prefix) {
        return cursor.getString(getColumnIndex(cursor, columnName, prefix));
    }

    public static Integer getInt(Cursor cursor, String columnName, String prefix) {
        return cursor.getInt(getColumnIndex(cursor, columnName, prefix));
    }

    public static boolean getBoolean(Cursor cursor, String columnName, String prefix) {
        int ret = getInt(cursor, columnName, prefix);
        return ret > 0 ? true : false;
    }

    public static String getDateForDb(String displayDate) {
        try {
            Date date = dateFormatForDisplay.parse(displayDate);
            return dateFormatForDb.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateForDisplay(Cursor cursor, String columnName, String prefix) {
        String dateString = cursor.getString(getColumnIndex(cursor, columnName, prefix));
        if(dateString == null || dateString.isEmpty()) {
            return "";
        }

        try {
            Date date = dateFormatForDb.parse(dateString);
            dateString = dateFormatForDisplay.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static long getLong(Cursor cursor, String columnName, String prefix) {
        return cursor.getLong(getColumnIndex(cursor, columnName, prefix));
    }

    private static int getColumnIndex(Cursor cursor, String columnName, String prefix) {
        return cursor.getColumnIndexOrThrow(prefix + "_" + columnName);
    }

    public static List<Photo> getPhotosFromProvider(ContentProviderClient contentProvider) throws RemoteException {
        Cursor cursor = contentProvider.query(ApparelContract.Photos.CONTENT_URI, ApparelContract.Photos.PROJECTION_ALL, null, null, null);
        List<Photo> photos = new ArrayList<>();

        while(cursor.moveToNext()) {
            Photo photo = new Photo();
            setCommonFieldsFromCursor(cursor, photo, DbSchema.PREFIX_TBL_PHOTOS);
            photo.setPhotoPath(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
            photo.setPhotoPathSmall(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));
            photos.add(photo);
        }
        cursor.close();
        return photos;
    }

    public static List<Event> getEventsFromProvider(ContentProviderClient contentProvider) throws RemoteException {
        Cursor cursor = contentProvider.query(ApparelContract.Events.CONTENT_URI, ApparelContract.Events.PROJECTION_ALL, null, null, null);
        List<Event> events = getEventsFromCursor(cursor);
        cursor.close();
        return events;
    }

    public static List<Event> getEventsFromCursor(Cursor cursor) {
        List<Event> events = new ArrayList<>();
        while(cursor.moveToNext()) {
            Event event = new Event();
            SqlHelper.setCommonFieldsFromCursor(cursor, event, DbSchema.PREFIX_TBL_EVENTS);
            event.setTitle(SqlHelper.getString(cursor, ApparelContract.Events.TITLE, DbSchema.PREFIX_TBL_EVENTS));
            event.setLocation(SqlHelper.getString(cursor, ApparelContract.Events.LOCATION, DbSchema.PREFIX_TBL_EVENTS));
            event.setOwnerUuid(SqlHelper.getString(cursor, ApparelContract.Events.OWNER_UUID, DbSchema.PREFIX_TBL_EVENTS));
            event.setStartDate(SqlHelper.getDateForDisplay(cursor, ApparelContract.Events.START_DATE, DbSchema.PREFIX_TBL_EVENTS));
            event.setEndDate(SqlHelper.getDateForDisplay(cursor, ApparelContract.Events.END_DATE, DbSchema.PREFIX_TBL_EVENTS));
            event.setDescription(SqlHelper.getString(cursor, ApparelContract.Events.DESCRIPTION, DbSchema.PREFIX_TBL_EVENTS));
            event.setPhotoUuid(SqlHelper.getString(cursor, ApparelContract.Photos.UUID, DbSchema.PREFIX_TBL_PHOTOS));
            User owner = new User();
            SqlHelper.setCommonFieldsFromCursor(cursor, owner, DbSchema.PREFIX_TBL_USERS);
            owner.setFacebookId(SqlHelper.getString(cursor, ApparelContract.Users.FACEBOOK_ID, DbSchema.PREFIX_TBL_USERS));
            owner.setName(SqlHelper.getString(cursor, ApparelContract.Users.NAME, DbSchema.PREFIX_TBL_USERS));
            event.setOwner(owner);
            Photo photo = new Photo();
            SqlHelper.setCommonFieldsFromCursor(cursor, photo, DbSchema.PREFIX_TBL_PHOTOS);
            photo.setPhotoPath(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
            photo.setPhotoPathSmall(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));
            event.setPhoto(photo);

            events.add(event);
        }

        return events;
    }

    public static List<Item> getItemsFromProvider(ContentProviderClient contentProvider) throws RemoteException {
        Cursor cursor = contentProvider.query(ApparelContract.Items.CONTENT_URI, ApparelContract.Items.PROJECTION_ALL, null, null, null);
        List<Item> items = getItemsFromCursor(cursor);
        cursor.close();
        return items;
    }

    public static List<Item> getItemsFromCursor(Cursor cursor) {
        List<Item> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            Item item = new Item();
            setCommonFieldsFromCursor(cursor, item, DbSchema.PREFIX_TBL_ITEMS);
            item.setName(SqlHelper.getString(cursor, ApparelContract.Items.NAME, DbSchema.PREFIX_TBL_ITEMS));
            item.setDescription(SqlHelper.getString(cursor, ApparelContract.Items.DESCRIPTION, DbSchema.PREFIX_TBL_ITEMS));
            item.setItemCategory(ItemCategory.valueOf(SqlHelper.getString(cursor, ApparelContract.Items.ITEM_CATEGORY, DbSchema.PREFIX_TBL_ITEMS)));
            item.setPhotoUuid(SqlHelper.getString(cursor, ApparelContract.Items.PHOTO_UUID, DbSchema.PREFIX_TBL_ITEMS));
            item.setUserUuid(SqlHelper.getString(cursor, ApparelContract.Items.USER_UUID, DbSchema.PREFIX_TBL_ITEMS));

            Photo photo = new Photo();
            setCommonFieldsFromCursor(cursor, photo, DbSchema.PREFIX_TBL_PHOTOS);
            photo.setPhotoPath(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH, DbSchema.PREFIX_TBL_PHOTOS));
            photo.setPhotoPathSmall(SqlHelper.getString(cursor, ApparelContract.Photos.LOCAL_PATH_SM, DbSchema.PREFIX_TBL_PHOTOS));
            item.setPhoto(photo);

            items.add(item);
        }

        return items;
    }

    public static void setCommonFieldsFromCursor(Cursor cursor, Entity entity, String prefix) {
        entity.setId(SqlHelper.getLong(cursor, ApparelContract.CommonColumns._ID, prefix));
        entity.setUuid(SqlHelper.getString(cursor, ApparelContract.CommonColumns.UUID, prefix));
        entity.setMarkedForDelete(SqlHelper.getBoolean(cursor, ApparelContract.CommonColumns.MARKED_FOR_DELETE, prefix));
        entity.setVersion(SqlHelper.getInt(cursor, ApparelContract.CommonColumns.VERSION, prefix));
    }

}
