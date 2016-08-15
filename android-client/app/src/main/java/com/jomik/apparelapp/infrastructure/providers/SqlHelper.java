package com.jomik.apparelapp.infrastructure.providers;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
