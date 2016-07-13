package com.jomik.apparelapp.infrastructure.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/4/2016.
 */
public class SqlOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = DbSchema.DB_NAME;
    private static final int VERSION = 14;

    public SqlOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbSchema.DDL_CREATE_TBL_ITEMS);
        db.execSQL(DbSchema.DDL_CREATE_TBL_EVENTS);
        db.execSQL(DbSchema.DDL_CREATE_TBL_EVENT_GUESTS);
        db.execSQL(DbSchema.DDL_CREATE_TBL_USERS);
        db.execSQL(DbSchema.DDL_INSERT_USER_RECORD, new Object[] {UUID.randomUUID().toString()});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Do  not do this in production!
        db.execSQL(DbSchema.DDL_DROP_TBL_ITEMS);
        db.execSQL(DbSchema.DDL_DROP_TBL_EVENTS);
        db.execSQL(DbSchema.DDL_DROP_TBL_EVENT_GUESTS);
        db.execSQL(DbSchema.DDL_DROP_TBL_USERS);

        onCreate(db);
    }
}
