package com.jomik.apparelapp.infrastructure.rest;

import com.jomik.apparelapp.domain.entities.Event;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
public interface RestService {

    String BASE_URL = "https://apparelapp.herokuapp.com";

    @GET("users/{uuid}")
    Call<DownloadSyncDto> getUserData(@Path("uuid") String userUuid);

    @POST("users/{uuid}")
    Call<ResponseBody> saveUserData(@Path("uuid") String userUuid, @Body UploadSyncDto syncDto);

    @Multipart
    @POST("photos/{uuid}")
    Call<ResponseBody> upload(@Path("uuid") String uuid, @Part MultipartBody.Part file);

    @GET("events")
    Call<List<Event>> searchEvents(@Query("keyword") String keyword, @Query("event_type") String eventType);
}
