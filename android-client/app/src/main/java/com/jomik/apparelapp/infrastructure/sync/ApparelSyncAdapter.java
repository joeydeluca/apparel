package com.jomik.apparelapp.infrastructure.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.providers.ApparelContract;
import com.jomik.apparelapp.infrastructure.providers.SqlHelper;
import com.jomik.apparelapp.infrastructure.rest.RestService;
import com.jomik.apparelapp.infrastructure.rest.SyncDto;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/1/2016.
 */
public class ApparelSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "ApparelSyncAdapter";

    private RestService restService;
    private ContentResolver contentResolver;

    public ApparelSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        init(context);
    }

    public ApparelSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        init(context);
    }

    private void init(Context c) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apparelapp.herokuapp.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
        contentResolver = c.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Start Sync Adapter");

        try {

            User user = AuthenticationManager.getAuthenticatedUser(getContext());
            String appUserUuid = user.getUuid();

            // get data from server
            SyncDto syncDtoResponse = restService.getUserData(appUserUuid).execute().body();
            if(syncDtoResponse == null) syncDtoResponse = new SyncDto();


            // Items
            List<Item> existingLocalItems = SqlHelper.getItemsFromProvider(provider);
            Set<Item> existingRemoteItems = syncDtoResponse.getItems();
            Set<Item> newLocalItems = getNewLocalEntities(existingLocalItems, existingRemoteItems);
            Set<Item> newRemoteItems = getNewRemoteEntities(existingLocalItems, existingRemoteItems);
            mergeEntitiesWithDuplicateUuids(existingLocalItems, existingRemoteItems, newLocalItems, newRemoteItems);
            saveToDb(newLocalItems, ApparelContract.Items.CONTENT_URI);

            // Photos
            List<Photo> existingLocalPhotos = getPhotosFromItems(existingLocalItems);
            Set<Photo> existingRemotePhotos = syncDtoResponse.getPhotos();
            Set<Photo> newLocalPhotos = getNewLocalEntities(existingLocalPhotos, existingRemotePhotos);
            Set<Photo> newRemotePhotos = getNewRemoteEntities(existingLocalPhotos, existingRemotePhotos);
            mergeEntitiesWithDuplicateUuids(existingLocalPhotos, existingRemotePhotos, newLocalPhotos, newRemotePhotos);
            saveToDb(newLocalPhotos, ApparelContract.Photos.CONTENT_URI);

            SyncDto syncDtoRequest = new SyncDto();
            syncDtoRequest.setItems(newRemoteItems);
            syncDtoRequest.setPhotos(newRemotePhotos);

            //String authToken = accountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            contentResolver.notifyChange(ApparelContract.CONTENT_URI, null);

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        Log.i(TAG, "End Sync Adapter");
    }

    private <T> Set<T> getNewRemoteEntities(List<T> existingLocalEntities, Set<T> existingRemoteEntities) {
        Set<T> newRemoteEntities = new HashSet<>();

        for(T existingLocalEntity : existingLocalEntities) {
            if(!existingRemoteEntities.contains(existingLocalEntity)) {
                newRemoteEntities.add(existingLocalEntity);
            }
        }

        return newRemoteEntities;
    }

    private <T> Set<T> getNewLocalEntities(List<T> existingLocalEntities, Set<T> existingRemoteEntities) {
        Set<T> newLocalEntities = new HashSet<>();

        for(T existingRemoteEntity : existingRemoteEntities) {
            if(!existingLocalEntities.contains(existingRemoteEntity)) {
                newLocalEntities.add(existingRemoteEntity);
            }
        }

        return newLocalEntities;
    }

    private void mergeEntitiesWithDuplicateUuids(List<? extends Entity> existingLocalEntities, Set<? extends Entity> existingRemoteEntities,
                       Set newLocalEntities, Set newRemoteEntities) {

        for(Entity existingRemoteEntity : existingRemoteEntities) {
            int index = existingLocalEntities.indexOf(existingRemoteEntity);
            if(index >= 0) {
                Entity existingLocalEntity = existingLocalEntities.get(index);

                // highest version wins
                if(existingLocalEntity.getVersion() < existingRemoteEntity.getVersion()) {
                    newLocalEntities.add(existingRemoteEntity);
                } else if(existingLocalEntity.getVersion() > existingRemoteEntity.getVersion()) {
                    newRemoteEntities.add(existingLocalEntity);
                }
            }
        }
    }

    private void saveToDb(Set<? extends Entity> entities, Uri contentUri) {
        for(Entity entity : entities) {
            ContentValues values = entity.getContentValues();
            int rowsUpdated = contentResolver.update(contentUri, values, "uuid = ?", new String[]{entity.getUuid()});
            if(rowsUpdated == 0) {
                contentResolver.insert(ApparelContract.Items.CONTENT_URI, values);
            }
        }
    }

    private void saveRemoteItems(SyncDto syncDto) {

    }

    private List<Photo> getPhotosFromItems(List<Item> items) {
        List<Photo> photos = new ArrayList<>();
        for(Item item : items) {
            photos.add(item.getPhoto());
        }
        return photos;
    }
}
