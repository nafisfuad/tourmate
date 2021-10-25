package com.nafisfuad.firebasemad16.serviceapi;

import com.nafisfuad.firebasemad16.currentweather.CurrentWeatherResponseBody;
import com.nafisfuad.firebasemad16.forecastweather.ForecastWeatherResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {
    @GET()
    Call<CurrentWeatherResponseBody> getCurrentWeather(@Url String endUrl);

    @GET()
    Call<ForecastWeatherResponseBody> getForecastWeather(@Url String endUrl);
}
