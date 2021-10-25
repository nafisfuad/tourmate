package com.nafisfuad.firebasemad16.repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.nafisfuad.firebasemad16.helper.RetrofitClient;
import com.nafisfuad.firebasemad16.nearby.NearbyResponseBody;
import com.nafisfuad.firebasemad16.serviceapi.NearbyServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyRepository {
    private static final String TAG = NearbyRepository.class.getSimpleName();
    private MutableLiveData<NearbyResponseBody> nearbyLD = new MutableLiveData<>();
    public MutableLiveData<NearbyResponseBody> getResponse(String endUrl) {
        NearbyServiceApi serviceApi = RetrofitClient.getClientForNearbyPlaces().create(NearbyServiceApi.class);
        serviceApi.getNearbyResponse(endUrl).enqueue(new Callback<NearbyResponseBody>() {
            @Override
            public void onResponse(Call<NearbyResponseBody> call, Response<NearbyResponseBody> response) {
                if (response.isSuccessful()) {
                    nearbyLD.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NearbyResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
        return nearbyLD;
    }
}
