package com.jomik.apparelapp.infrastructure.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.jomik.apparelapp.domain.entities.Entity;
import com.jomik.apparelapp.domain.entities.Event;
import com.jomik.apparelapp.domain.entities.EventGuest;
import com.jomik.apparelapp.domain.entities.EventGuestOutfit;
import com.jomik.apparelapp.domain.entities.EventGuestOutfitItem;
import com.jomik.apparelapp.domain.entities.Item;
import com.jomik.apparelapp.domain.entities.Photo;
import com.jomik.apparelapp.domain.entities.User;
import com.jomik.apparelapp.infrastructure.ormlite.OrmLiteSqlHelper;
import com.jomik.apparelapp.infrastructure.rest.DownloadSyncDto;
import com.jomik.apparelapp.infrastructure.rest.RestService;
import com.jomik.apparelapp.infrastructure.rest.UploadSyncDto;
import com.jomik.apparelapp.infrastructure.services.AuthenticationManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Joe Deluca of house targaryen, first his name, mother of dragons and breaker of chains on 7/1/2016.
 */
public class ApparelSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "ApparelSyncAdapter";

    private RestService restService;
    private ContentResolver contentResolver;
    private OrmLiteSqlHelper mHelper;

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
                .baseUrl(RestService.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
        contentResolver = c.getContentResolver();
        mHelper = new OrmLiteSqlHelper(c);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Start Sync Adapter");

        try {
            User user = AuthenticationManager.getAuthenticatedUser(getContext());
            if(user == null) {
                throw new Exception("Cannot find logged in user");
            }

            // get data from server
            Response<DownloadSyncDto> response = restService.getUserData(user.getFacebookId()).execute();
            if(!response.isSuccessful()) {
                Log.e(TAG, "Error downloading data from server. " + response.errorBody().string());
                return;
            }
            DownloadSyncDto downloadSyncDto = response.body();
            if(downloadSyncDto == null) downloadSyncDto = new DownloadSyncDto();
            UploadSyncDto uploadSyncDto = new UploadSyncDto();

            // User
            User remoteUser = downloadSyncDto.getUser();
            if(remoteUser == null) {
                uploadSyncDto.setUser(user);
            }

            // Items
            List<Item> existingLocalItems = mHelper.getItemDao().queryForAll();
            Set<Item> existingRemoteItems = downloadSyncDto.getItems();
            existingRemoteItems.addAll(getItemsFromEventsGuestOutfitItems(downloadSyncDto.getEventGuestOutfitItems()));
            Set<Item> newLocalItems = getNewLocalEntities(existingLocalItems, existingRemoteItems);
            Set<Item> newRemoteItems = getNewRemoteEntities(existingLocalItems, existingRemoteItems);
            mergeEntitiesWithDuplicateUuids(existingLocalItems, existingRemoteItems, newLocalItems, newRemoteItems);

            // Events
            List<Event> existingLocalEvents = mHelper.getEventDao().queryForAll();
            Set<Event> existingRemoteEvents = downloadSyncDto.getEvents();
            Set<Event> newLocalEvents = getNewLocalEntities(existingLocalEvents, existingRemoteEvents);
            Set<Event> newRemoteEvents = getNewRemoteEntities(existingLocalEvents, existingRemoteEvents);
            mergeEntitiesWithDuplicateUuids(existingLocalEvents, existingRemoteEvents, newLocalEvents, newRemoteEvents);

            // Event Guests
            List<EventGuest> existingLocalEventGuests = mHelper.getEventGuestDao().queryForAll();
            Set<EventGuest> existingRemoteEventGuests = downloadSyncDto.getEventGuests();
            Set<EventGuest> newLocalEventGuests = getNewLocalEntities(existingLocalEventGuests, existingRemoteEventGuests);
            Set<EventGuest> newRemoteEventGuests = getNewRemoteEntities(existingLocalEventGuests, existingRemoteEventGuests);
            mergeEntitiesWithDuplicateUuids(existingLocalEventGuests, existingRemoteEventGuests, newLocalEventGuests, newRemoteEventGuests);

            // Event Guest Outfits
            List<EventGuestOutfit> existingLocalEventGuestOutfits = mHelper.getEventGuestOutfitDao().queryForAll();
            Set<EventGuestOutfit> existingRemoteEventGuestOutfits = downloadSyncDto.getEventGuestOutfits();
            Set<EventGuestOutfit> newLocalEventGuestOutfits = getNewLocalEntities(existingLocalEventGuestOutfits, existingRemoteEventGuestOutfits);
            Set<EventGuestOutfit> newRemoteEventGuestOutfits = getNewRemoteEntities(existingLocalEventGuestOutfits, existingRemoteEventGuestOutfits);
            mergeEntitiesWithDuplicateUuids(existingLocalEventGuestOutfits, existingRemoteEventGuestOutfits, newLocalEventGuestOutfits, newRemoteEventGuestOutfits);

            // Photos
            List<Photo> existingLocalPhotos = mHelper.getPhotoDao().queryForAll();
            Set<Photo> existingRemotePhotos = new HashSet<>();
            existingRemotePhotos.addAll(getPhotosFromItems(existingRemoteItems));
            existingRemotePhotos.addAll(getPhotosFromEvents(existingRemoteEvents));
            existingRemotePhotos.addAll(getPhotosFromEventsGuestOutfitItems(downloadSyncDto.getEventGuestOutfitItems()));
            Set<Photo> newLocalPhotos = getNewLocalEntities(existingLocalPhotos, existingRemotePhotos);
            Set<Photo> newRemotePhotos = getNewRemoteEntities(existingLocalPhotos, existingRemotePhotos);
            mergeEntitiesWithDuplicateUuids(existingLocalPhotos, existingRemotePhotos, newLocalPhotos, newRemotePhotos);

            // Guests
            List<User> existingLocalGuests = mHelper.getUserDao().queryForAll();
            Set<User> existingRemoteGuests = getRemoteGuests(newLocalEvents, newLocalEventGuests);
            Set<User> newLocalGuests = getNewLocalEntities(existingLocalGuests, existingRemoteGuests);

            // Save to local db
            saveToDb(mHelper.getUserDao(), newLocalGuests);
            saveToDb(mHelper.getPhotoDao(), newLocalPhotos);
            saveToDb(mHelper.getItemDao(), newLocalItems);
            saveToDb(mHelper.getEventDao(), newLocalEvents);
            saveToDb(mHelper.getEventGuestDao(), newLocalEventGuests);
            saveEventGuestOutfitsToDb(newLocalEventGuestOutfits, downloadSyncDto.getEventGuestOutfitItems());

            // Upload to server
            uploadSyncDto.setItems(newRemoteItems);
            uploadSyncDto.setEvents(newRemoteEvents);
            uploadSyncDto.setPhotos(newRemotePhotos);
            uploadSyncDto.setEventGuests(newRemoteEventGuests);
            uploadSyncDto.setEventGuestOutfits(newRemoteEventGuestOutfits);
            uploadSyncDto.setEventGuestOutfitItems(newRemoteEventGuestOutfits);
            uploadRemoteItems(user, uploadSyncDto);

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

    private void saveToDb(Dao dao, Set<? extends Entity> entities) throws SQLException {
        for(Entity entity : entities) {
            Log.i(TAG, "Saving " + entity.getClass().getSimpleName() + " uuid: " + entity.getUuid() );
            dao.createOrUpdate(entity);
        }
    }

    private void saveEventGuestOutfitsToDb(Set<EventGuestOutfit> eventGuestOutfits, Set<EventGuestOutfitItem> eventGuestOutfitItems) throws SQLException {
        for(EventGuestOutfit eventGuestOutfit : eventGuestOutfits) {
            Log.i(TAG, "Saving " + eventGuestOutfit.getClass().getSimpleName() + " uuid: " + eventGuestOutfit.getUuid());

            // Save outfit
            mHelper.getEventGuestOutfitDao().createOrUpdate(eventGuestOutfit);

            // Delete existing outfit items
            DeleteBuilder<EventGuestOutfitItem, String> deleteBuilder = mHelper.getEventGuestOutfitItemDao().deleteBuilder();
            deleteBuilder.where().eq("event_guest_outfit_uuid", eventGuestOutfit.getUuid());
            deleteBuilder.delete();

            // Create new outfit items
            for(EventGuestOutfitItem eventGuestOutfitItem : eventGuestOutfitItems) {
                if(eventGuestOutfit.getUuid().equals(eventGuestOutfitItem.getEventGuestOutfit().getUuid())) {
                    mHelper.getItemDao().createIfNotExists(eventGuestOutfitItem.getItem());
                    mHelper.getEventGuestOutfitItemDao().create(eventGuestOutfitItem);
                }
            }
        }
    }

    private void uploadRemoteItems(User user, UploadSyncDto syncDto) throws IOException {
        if(syncDto.canUpload()) {
            // Upload data
            Log.i(TAG, "Uploading data");
            Response response = restService.saveUserData(user.getUuid(), syncDto).execute();
            if(response.isSuccessful()) Log.i(TAG, "Data upload successful");
            else Log.e(TAG, "Data upload failed");

            // Upload photo files
            for(Photo photo : syncDto.getPhotos()) {
                Log.i(TAG, "Uploading photo " + photo.getUuid());
                File file = new File(photo.getPhotoPath());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                response = restService.upload(photo.getUuid(), body).execute();
                if(response.isSuccessful()) Log.i(TAG, "Photo upload successful");
                else Log.e(TAG, "Photo upload failed");
            }
        }
    }

    private List<Photo> getPhotosFromItems(Collection<Item> items) {
        List<Photo> photos = new ArrayList<>();
        for(Item item : items) {
            if(item.getPhoto() != null) {
                photos.add(item.getPhoto());
            }
        }
        return photos;
    }

    private Set<Item> getItemsFromEventsGuestOutfitItems(Collection<EventGuestOutfitItem> eventGuestOutfitItems) {
        Set<Item> items = new HashSet<>();
        for(EventGuestOutfitItem eventGuestOutfitItem : eventGuestOutfitItems) {
            if(eventGuestOutfitItem.getItem().getPhoto() != null) {
                items.add(eventGuestOutfitItem.getItem());
            }
        }
        return items;
    }

    private List<Photo> getPhotosFromEventsGuestOutfitItems(Collection<EventGuestOutfitItem> eventGuestOutfitItems) {
        List<Photo> photos = new ArrayList<>();
        for(EventGuestOutfitItem eventGuestOutfitItem : eventGuestOutfitItems) {
            if(eventGuestOutfitItem.getItem().getPhoto() != null) {
                photos.add(eventGuestOutfitItem.getItem().getPhoto());
            }
        }
        return photos;
    }

    private List<Photo> getPhotosFromEvents(Collection<Event> events) {
        List<Photo> photos = new ArrayList<>();
        for(Event event : events) {
            if(event.getPhoto() != null) {
                photos.add(event.getPhoto());
            }
        }
        return photos;
    }

    private Set<User> getRemoteGuests(Set<Event> newLocalEvents, Set<EventGuest> newLocalEventGuests) {
        Set<User> existingRemoteGuests = new HashSet<>();
        for(Event event : newLocalEvents) {
            existingRemoteGuests.add(event.getOwner());
        }
        for(EventGuest eventGuest : newLocalEventGuests) {
            existingRemoteGuests.add(eventGuest.getGuest());
        }

        return existingRemoteGuests;
    }
}
