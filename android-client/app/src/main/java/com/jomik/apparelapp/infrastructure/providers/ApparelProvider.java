package com.jomik.apparelapp.infrastructure.providers;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.jomik.apparelapp.BuildConfig;

import java.util.ArrayList;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/2/2016.
 */
public class ApparelProvider extends ContentProvider {
    private static final UriMatcher URI_MATCHER;
    private static final int ITEM_LIST = 1;
    private static final int ITEM_ID = 2;
    private static final int EVENT_LIST = 3;
    private static final int EVENT_ID = 4;
    private static final int EVENT_GUEST_LIST = 5;
    private static final int EVENT_GUEST_ID = 6;
    private static final int EVENT_GUEST_OUTFIT_LIST = 7;
    private static final int EVENT_GUEST_OUTFIT_ID = 8;
    private static final int EVENT_GUEST_OUTFIT_ITEM_LIST = 9;
    private static final int EVENT_GUEST_OUTFIT_ITEM_ID = 10;
    private static final int PHOTO_LIST = 11;
    private static final int PHOTO_ID = 12;

    private final ThreadLocal<Boolean> isInBatchMode = new ThreadLocal<Boolean>();
    private SqlOpenHelper sqlOpenHelper;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "photos", PHOTO_LIST);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "photos/#", PHOTO_ID);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "items", ITEM_LIST);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "items/#", ITEM_ID);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "events", EVENT_LIST);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "events/#", EVENT_ID);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "event_guests", EVENT_GUEST_LIST);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "event_guests/#", EVENT_GUEST_ID);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "event_guest_outfits", EVENT_GUEST_OUTFIT_LIST);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "event_guest_outfits/#", EVENT_GUEST_OUTFIT_ID);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "event_guest_outfit_items", EVENT_GUEST_OUTFIT_ITEM_LIST);
        URI_MATCHER.addURI(ApparelContract.AUTHORITY, "event_guest_outfit_items/#", EVENT_GUEST_OUTFIT_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        sqlOpenHelper = new SqlOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            SQLiteDatabase db = sqlOpenHelper.getReadableDatabase();
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            boolean useAuthorityUri = false;
            switch (URI_MATCHER.match(uri)) {
                case ITEM_LIST:
                    builder.setTables(DbSchema.FROM_ITEMS);
                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = ApparelContract.Items.SORT_ORDER_DEFAULT;
                    }
                    break;
                case ITEM_ID:
                    builder.setTables(DbSchema.FROM_ITEMS);
                    builder.appendWhere(DbSchema.PREFIX_TBL_ITEMS + "." + ApparelContract.Items._ID + " = "
                            + uri.getLastPathSegment());
                    break;
                case EVENT_LIST:
                    builder.setTables(DbSchema.FROM_EVENTS);
                    break;
                case EVENT_ID:
                    builder.setTables(DbSchema.FROM_EVENTS);
                    // limit query to one row at most:
                    builder.appendWhere(DbSchema.PREFIX_TBL_EVENTS + "." + ApparelContract.Events._ID + " = " + uri.getLastPathSegment());
                    break;
                case EVENT_GUEST_LIST:
                    //builder.setTables(DbSchema.TBL_EVENT_GUESTS + " g join events e on e.uuid = g.event_uuid join users u on u.uuid = g.guest_uuid left join event_guest_outfits o on o.event_guest_uuid = g.uuid left join event_guest_outfit_items oi on oi.event_guest_outfit_uuid = o.uuid left join items i on i.uuid = oi.item_uuid");
                    builder.setTables(DbSchema.FROM_EVENT_GUESTS);
                    break;
                case EVENT_GUEST_ID:
                    builder.setTables(DbSchema.TBL_EVENTS);
                    // limit query to one row at most:
                    builder.appendWhere(ApparelContract.EventGuests._ID + " = " + uri.getLastPathSegment());
                    break;
                case EVENT_GUEST_OUTFIT_LIST:
                    builder.setTables(DbSchema.TBL_EVENT_GUEST_OUTFITS);
                    break;
                case EVENT_GUEST_OUTFIT_ITEM_LIST:
                    builder.setTables(DbSchema.TBL_EVENT_GUEST_OUTFIT_ITEMS + " oi join items i on i.uuid = oi.item_uuid");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
            // if you like you can log the query
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                logQuery(builder,  projection, selection, sortOrder);
            }
            else {
                logQueryDeprecated(builder, projection, selection, sortOrder);
            }
            Cursor cursor = builder.query(db, projection, selection, selectionArgs,
                    null, null, sortOrder);

            // if we want to be notified of any changes:
            if (useAuthorityUri) {
                cursor.setNotificationUri(getContext().getContentResolver(), ApparelContract.CONTENT_URI);
            }
            else {
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
            }

            return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                return ApparelContract.Items.CONTENT_TYPE;
            case ITEM_ID:
                return ApparelContract.Items.CONTENT_ITEM_TYPE;
            case EVENT_ID:
                return ApparelContract.Events.CONTENT_EVENT_TYPE;
            case EVENT_LIST:
                return ApparelContract.Events.CONTENT_TYPE;
            case EVENT_GUEST_OUTFIT_ID:
                return ApparelContract.EventGuestOutfits.CONTENT_EVENT_TYPE;
            case EVENT_GUEST_OUTFIT_LIST:
                return ApparelContract.EventGuestOutfits.CONTENT_TYPE;
            case EVENT_GUEST_OUTFIT_ITEM_ID:
                return ApparelContract.EventGuestOutfitItems.CONTENT_EVENT_TYPE;
            case EVENT_GUEST_OUTFIT_ITEM_LIST:
                return ApparelContract.EventGuestOutfitItems.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = sqlOpenHelper.getWritableDatabase();
        if (URI_MATCHER.match(uri) == PHOTO_LIST) {
            long id = db.insert(DbSchema.TBL_PHOTOS, null, values);
            return getUriForId(id, uri);
        }else if (URI_MATCHER.match(uri) == ITEM_LIST) {
            long id = db.insert(DbSchema.TBL_ITEMS, null, values);
            return getUriForId(id, uri);
        } else if (URI_MATCHER.match(uri) == EVENT_LIST) {
            long id = db.insert(DbSchema.TBL_EVENTS, null, values);
            return getUriForId(id, uri);
        } else if (URI_MATCHER.match(uri) == EVENT_GUEST_LIST) {
            long id = db.insert(DbSchema.TBL_EVENT_GUESTS, null, values);
            return getUriForId(id, uri);
        } else if (URI_MATCHER.match(uri) == EVENT_GUEST_OUTFIT_LIST) {
            long id = db.insert(DbSchema.TBL_EVENT_GUEST_OUTFITS, null, values);
            return getUriForId(id, uri);
        } else if (URI_MATCHER.match(uri) == EVENT_GUEST_OUTFIT_ITEM_LIST) {
            long id = db.insert(DbSchema.TBL_EVENT_GUEST_OUTFIT_ITEMS, null, values);
            return getUriForId(id, uri);
        }

        throw new IllegalArgumentException(
                "Unsupported URI for insertion: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = sqlOpenHelper.getWritableDatabase();
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case PHOTO_LIST:
                delCount = db.delete(DbSchema.TBL_PHOTOS, selection, selectionArgs);
                break;
            case PHOTO_ID:
                delCount = db.delete(DbSchema.TBL_PHOTOS, getWhereClause(uri, selection), selectionArgs);
                break;
            case ITEM_LIST:
                delCount = db.delete(DbSchema.TBL_ITEMS, selection, selectionArgs);
                break;
            case ITEM_ID:
                delCount = db.delete(DbSchema.TBL_ITEMS, getWhereClause(uri, selection), selectionArgs);
                break;
            case EVENT_LIST:
                delCount = db.delete(DbSchema.TBL_EVENTS, selection, selectionArgs);
                break;
            case EVENT_ID:
                delCount = db.delete(DbSchema.TBL_EVENTS, getWhereClause(uri, selection), selectionArgs);
                break;
            case EVENT_GUEST_OUTFIT_ITEM_LIST:
                delCount = db.delete(DbSchema.FROM_EVENT_GUEST_OUTFIT_ITEMS, selection, selectionArgs);
                break;
            case EVENT_GUEST_OUTFIT_ITEM_ID:
                delCount = db.delete(DbSchema.TBL_EVENT_GUEST_OUTFIT_ITEMS, getWhereClause(uri, selection), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // notify all listeners of changes:
        if (delCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = sqlOpenHelper.getWritableDatabase();
        int updateCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                updateCount = db.update(DbSchema.TBL_ITEMS, values, selection, selectionArgs);
                break;
            case ITEM_ID:
                updateCount = db.update(DbSchema.TBL_ITEMS, values, getWhereClause(uri, selection), selectionArgs);
                break;
            case EVENT_LIST:
                updateCount = db.update(DbSchema.TBL_EVENTS, values, selection, selectionArgs);
                break;
            case EVENT_ID:
                updateCount = db.update(DbSchema.TBL_EVENTS, values, getWhereClause(uri, selection), selectionArgs);
                break;
            case EVENT_GUEST_OUTFIT_LIST:
                updateCount = db.update(DbSchema.TBL_EVENT_GUEST_OUTFITS, values, selection, selectionArgs);
                break;
            case EVENT_GUEST_OUTFIT_ID:
                updateCount = db.update(DbSchema.TBL_EVENT_GUEST_OUTFITS, values, getWhereClause(uri, selection), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // notify all listeners of changes:
        if (updateCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase db = sqlOpenHelper.getWritableDatabase();
        isInBatchMode.set(true);
        db.beginTransaction();
        try {
            final ContentProviderResult[] retResult = super.applyBatch(operations);
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(ApparelContract.CONTENT_URI, null);
            return retResult;
        }
        finally {
            isInBatchMode.remove();
            db.endTransaction();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void logQuery(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder) {
        if (BuildConfig.DEBUG) {
            Log.v("apparel", "query: " + builder.buildQuery(projection, selection, null, null, sortOrder, null));
        }
    }

    @SuppressWarnings("deprecation")
    private void logQueryDeprecated(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder) {
        if (BuildConfig.DEBUG) {
            Log.v("apparel", "query: " + builder.buildQuery(projection, selection, null, null, null, sortOrder, null));
        }
    }

    private String getWhereClause(Uri uri, String selection) {
        String idStr = uri.getLastPathSegment();
        String where = BaseColumns._ID + " = " + idStr;
        if (!TextUtils.isEmpty(selection)) {
            where += " AND " + selection;
        }
        return where;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                // notify all listeners of changes and return itemUri:
                getContext().
                        getContentResolver().
                        notifyChange(itemUri, null);
            }
            return itemUri;
        }
        // something went wrong:
        throw new SQLException("Problem while inserting into uri: " + uri);
    }

    private boolean isInBatchMode() {
        return isInBatchMode.get() != null && isInBatchMode.get();
    }
}
