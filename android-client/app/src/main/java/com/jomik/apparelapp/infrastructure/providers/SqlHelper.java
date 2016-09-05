package com.jomik.apparelapp.infrastructure.providers;

import android.content.ContentProviderClient;
import android.database.Cursor;
import android.os.RemoteException;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;

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

    public static final String dateFormatForDisplayPattern = "MMM dd, yyyy";
    public static final String dateFormatForDbPattern = "dd-MM-yyyy";
    public static DateFormat dateFormatForDisplay = new SimpleDateFormat(dateFormatForDisplayPattern);
    public static DateFormat dateFormatForDb = new SimpleDateFormat(dateFormatForDbPattern);

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

    public static String getDateForDb(Date date) {
        return dateFormatForDb.format(date);
    }

    public static Date getDateFromCursor(Cursor cursor, String columnName, String prefix) {
        String dateString = cursor.getString(getColumnIndex(cursor, columnName, prefix));
        if(dateString == null || dateString.isEmpty()) {
            return new Date();
        }

        try {
            return dateFormatForDb.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
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

    /*public static List<Photo> getPhotosFromProvider(ContentProviderClient contentProvider) throws RemoteException {
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
    }*/

    /*public static List<Event> getEventsFromProvider(ContentProviderClient contentProvider) throws RemoteException {
        Cursor cursor = contentProvider.query(ApparelContract.Events.CONTENT_URI, ApparelContract.Events.PROJECTION_ALL, null, null, null);
        List<Event> events = getEventsFromCursor(cursor);
        cursor.close();
        return events;
    }*/


    /*public static List<Item> getItemsFromProvider(ContentProviderClient contentProvider) throws RemoteException {
        Cursor cursor = contentProvider.query(ApparelContract.Items.CONTENT_URI, ApparelContract.Items.PROJECTION_ALL, null, null, null);
        List<Item> items = getItemsFromCursor(cursor);
        cursor.close();
        return items;
    }*/

   /* public static List<Item> getItemsFromCursor(Cursor cursor) {
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
    }*/

    public static void printCursorRow(Cursor cursor) {
        System.out.println("===============================");
        if(cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                System.out.println(cursor.getColumnName(i) + ": " + cursor.getString(i));
            }
        }
        System.out.println("===============================");
    }

    /*public static void setCommonFieldsFromCursor(Cursor cursor, Entity entity, String prefix) {
        entity.setId(SqlHelper.getLong(cursor, ApparelContract.CommonColumns._ID, prefix));
        entity.setUuid(SqlHelper.getString(cursor, ApparelContract.CommonColumns.UUID, prefix));
        entity.setMarkedForDelete(SqlHelper.getBoolean(cursor, ApparelContract.CommonColumns.MARKED_FOR_DELETE, prefix));
        entity.setVersion(SqlHelper.getInt(cursor, ApparelContract.CommonColumns.VERSION, prefix));
    }*/

}
