package com.nafisfuad.firebasemad16.repos;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.nafisfuad.firebasemad16.currentweather.CurrentWeatherResponseBody;
import com.nafisfuad.firebasemad16.forecastweather.ForecastWeatherResponseBody;
import com.nafisfuad.firebasemad16.helper.RetrofitClient;
import com.nafisfuad.firebasemad16.serviceapi.WeatherServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private static final String TAG = WeatherRepository.class.getSimpleName();
    public MutableLiveData<CurrentWeatherResponseBody> currentResponseLD = new MutableLiveData<>();
    public MutableLiveData<ForecastWeatherResponseBody> forecastResponseLD = new MutableLiveData<>();

    public MutableLiveData<CurrentWeatherResponseBody> getCurrentWeather(Location location, String unit, String apiKey) {
        WeatherServiceApi serviceApi = RetrofitClient.getClientForWeather().create(WeatherServiceApi.class);
        String endUrl = String.format("data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s", location.getLatitude(), location.getLongitude(), unit, apiKey);
        serviceApi.getCurrentWeather(endUrl).enqueue(new Callback<CurrentWeatherResponseBody>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponseBody> call, Response<CurrentWeatherResponseBody> response) {
//                if (response.isSuccessful())
                if (response.code() == 200) {
                    CurrentWeatherResponseBody responseBody = response.body();
                    currentResponseLD.postValue(responseBody);
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
        return currentResponseLD;
    }

    public MutableLiveData<ForecastWeatherResponseBody> getForecastWeather(Location location, String unit, String apiKey) {
        WeatherServiceApi serviceApi = RetrofitClient.getClientForWeather().create(WeatherServiceApi.class);
        String endUrl = String.format("data/2.5/forecast/daily?lat=%f&lon=%f&cnt=7&units=%s&appid=%s", location.getLongitude(), location.getLongitude(), unit, apiKey);
        serviceApi.getForecastWeather(endUrl).enqueue(new Callback<ForecastWeatherResponseBody>() {
            @Override
            public void onResponse(Call<ForecastWeatherResponseBody> call, Response<ForecastWeatherResponseBody> response) {
                if (response.isSuccessful()) {
                    ForecastWeatherResponseBody responseBody = response.body();
                    forecastResponseLD.postValue(responseBody);
                }
            }

            @Override
            public void onFailure(Call<ForecastWeatherResponseBody> call, Throwable t) {

            }
        });

        return forecastResponseLD;
    }
}
