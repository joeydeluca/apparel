package com.jomik.apparelapp.infrastructure.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventGuestOutfit;
import com.jomik.apparelapp.domain.entities.EventGuestOutfitItem;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.providers.DbSchema;

import java.sql.SQLException;

/**
 * Created by Joe Deluca on 9/3/2016.
 */
public class OrmLiteSqlHelper extends OrmLiteSqliteOpenHelper {
    private static final String NAME = DbSchema.DB_NAME;
    private static final int VERSION = 42;

    private Dao<Photo, String> mPhotoDao = null;
    private Dao<User, String> mUserDao = null;
    private Dao<Item, String> mItemDao = null;
    private Dao<Event, String> mEventDao = null;
    private Dao<EventGuest, String> mEventGuestDao = null;
    private Dao<EventGuestOutfit, String> mEventGuestOutfitDao = null;
    private Dao<EventGuestOutfitItem, String> mEventGuestOutfitItemDao = null;

    public OrmLiteSqlHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.setWriteAheadLoggingEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Photo.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Item.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, EventGuest.class);
            TableUtils.createTable(connectionSource, EventGuestOutfit.class);
            TableUtils.createTable(connectionSource, EventGuestOutfitItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, EventGuestOutfitItem.class, false);
            TableUtils.dropTable(connectionSource, EventGuestOutfit.class, false);
            TableUtils.dropTable(connectionSource, EventGuest.class, false);
            TableUtils.dropTable(connectionSource, Event.class, false);
            TableUtils.dropTable(connectionSource, Item.class, false);
            TableUtils.dropTable(connectionSource, User.class, false);
            TableUtils.dropTable(connectionSource, Photo.class, false);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<User, String> getUserDao() throws SQLException {
        if (mUserDao == null) {
            mUserDao = getDao(User.class);
        }

        return mUserDao;
    }

    public Dao<Photo, String> getPhotoDao() throws SQLException {
        if (mPhotoDao == null) {
            mPhotoDao = getDao(Photo.class);
        }

        return mPhotoDao;
    }

    public Dao<Event, String> getEventDao() throws SQLException {
        if (mEventDao == null) {
            mEventDao = getDao(Event.class);
        }

        return mEventDao;
    }

    public Dao<EventGuestOutfit, String> getEventGuestOutfitDao() throws SQLException {
        if (mEventGuestOutfitDao == null) {
            mEventGuestOutfitDao = getDao(EventGuestOutfit.class);
        }

        return mEventGuestOutfitDao;
    }

    public Dao<EventGuest, String> getEventGuestDao() throws SQLException {
        if (mEventGuestDao == null) {
            mEventGuestDao = getDao(EventGuest.class);
        }

        return mEventGuestDao;
    }

    public Dao<Item, String> getItemDao() throws SQLException {
        if (mItemDao == null) {
            mItemDao = getDao(Item.class);
        }

        return mItemDao;
    }

    public Dao<EventGuestOutfitItem, String> getEventGuestOutfitItemDao() throws SQLException {
        if (mEventGuestOutfitItemDao == null) {
            mEventGuestOutfitItemDao = getDao(EventGuestOutfitItem.class);
        }

        return mEventGuestOutfitItemDao;
    }

    @Override
    public void close() {
        super.close();

        mItemDao = null;
        mUserDao = null;
        mEventGuestOutfitDao = null;
        mEventDao = null;
        mEventGuestDao = null;
        mEventGuestOutfitItemDao = null;
        mPhotoDao = null;
    }
}
