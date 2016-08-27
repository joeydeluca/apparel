package com.jomik.apparelapp.infrastructure.rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Joe Deluca on 8/24/2016.
 */
public interface RestService {

    @GET("users/{uuid}")
    Call<SyncDto> getUserData(@Path("uuid") String userUuid);

    @POST("users/{uuid}")
    Call<Object> saveUserData(@Path("uuid") String userUuid, @Body SyncDto syncDto);
}
