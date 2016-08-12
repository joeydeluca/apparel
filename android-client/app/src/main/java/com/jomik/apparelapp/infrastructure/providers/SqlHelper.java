package com.jomik.apparelapp.infrastructure.providers;

import android.database.Cursor;

/**
 * Created by Joe Deluca on 8/4/2016.
 */
public class SqlHelper {

    public static String getSelectColumn(String columnName) {
        return getSelectColumn(columnName, null);
    }

    public static String getSelectColumn(String columnName, String prefix) {
        return prefix == null ? columnName :  prefix + "." + columnName + " as '" + prefix + "_" + columnName + "'";
    }

    public static String getString(Cursor cursor, String columnName, String prefix) {
        return cursor.getString(getColumnIndex(cursor, columnName, prefix));
    }

    public static long getLong(Cursor cursor, String columnName, String prefix) {
        return cursor.getLong(getColumnIndex(cursor, columnName, prefix));
    }

    private static int getColumnIndex(Cursor cursor, String columnName, String prefix) {
        return cursor.getColumnIndexOrThrow(prefix + "_" + columnName);
    }

}
