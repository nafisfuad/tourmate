package com.nafisfuad.firebasemad16.viewmodels;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nafisfuad.firebasemad16.currentweather.CurrentWeatherResponseBody;
import com.nafisfuad.firebasemad16.forecastweather.ForecastWeatherResponseBody;
import com.nafisfuad.firebasemad16.repos.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    private WeatherRepository repository;
    public MutableLiveData<CurrentWeatherResponseBody> currentResponseLD;

    public WeatherViewModel() {
        repository = new WeatherRepository();
        currentResponseLD = new MutableLiveData<>();
    }

    public MutableLiveData<CurrentWeatherResponseBody> getCurrentWeather(Location location, String unit, String apikey) {
        currentResponseLD = repository.getCurrentWeather(location, unit, apikey);
        return currentResponseLD;
    }

    public MutableLiveData<ForecastWeatherResponseBody> getForecast(Location location, String unit, String apiKey) {
        return repository.getForecastWeather(location, unit, apiKey);
    }


}
