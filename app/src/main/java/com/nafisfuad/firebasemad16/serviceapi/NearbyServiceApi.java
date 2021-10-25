package com.nafisfuad.firebasemad16.serviceapi;

import com.nafisfuad.firebasemad16.nearby.NearbyResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyServiceApi {
    @GET
    Call<NearbyResponseBody> getNearbyResponse(@Url String endUrl);
}
