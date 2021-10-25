package com.nafisfuad.firebasemad16;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.nafisfuad.firebasemad16.currentweather.CurrentWeatherResponseBody;
import com.nafisfuad.firebasemad16.databinding.FragmentWeatherBinding;
import com.nafisfuad.firebasemad16.forecastweather.ForecastList;
import com.nafisfuad.firebasemad16.forecastweather.ForecastWeatherResponseBody;
import com.nafisfuad.firebasemad16.helper.EventUtils;
import com.nafisfuad.firebasemad16.viewmodels.LocationViewModel;
import com.nafisfuad.firebasemad16.viewmodels.WeatherViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private LocationViewModel locationViewModel;
    private WeatherViewModel weatherViewModel;
    private String unit = EventUtils.UNIT_CELCIUS;
    private String tempUnit = EventUtils.UNIT_CELCIUS_SYMBOL;
    private Location currentnLocation;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getContext(), getString(R.string.place_api_key));
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.weather_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.weather_item_search).getActionView();
        searchView.setQueryHint("search by city");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    convertQueryToLatLng(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void convertQueryToLatLng(String query) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = geocoder.getFromLocationName(query, 1);
        if (addressList != null && addressList.size() > 0) {
            double lat = addressList.get(0).getLatitude();
            double lng = addressList.get(0).getLongitude();
            currentnLocation.setLatitude(lat);
            currentnLocation.setLongitude(lng);
            initializeCurrentWeather(currentnLocation);
            initializeForecastWeather(currentnLocation);
        } else {
            Toast.makeText(getActivity(), "Please provide a valid city name", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weather_item_celcius:
                unit = EventUtils.UNIT_CELCIUS;
                tempUnit = EventUtils.UNIT_CELCIUS_SYMBOL;
                initializeCurrentWeather(currentnLocation);
                initializeForecastWeather(currentnLocation);
                break;
            case R.id.weather_item_fahrenheit:
                tempUnit = EventUtils.UNIT_FAHRENHEIT_SYMBOL;
                unit = EventUtils.UNIT_FAHRENHEIT;
                initializeCurrentWeather(currentnLocation);
                initializeForecastWeather(currentnLocation);
                break;
            case R.id.weather_item_search:
//                showPlaceAutocompleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPlaceAutocompleteDialog() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ID.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                currentnLocation.setLatitude(place.getLatLng().latitude);
                currentnLocation.setLongitude(place.getLatLng().longitude);
                initializeCurrentWeather(currentnLocation);
                initializeForecastWeather(currentnLocation);
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem celciusItem = menu.findItem(R.id.weather_item_celcius);
        MenuItem fahrenheitItem = menu.findItem(R.id.weather_item_fahrenheit);
        if (unit.equals(EventUtils.UNIT_CELCIUS)) {
            celciusItem.setVisible(false);
            fahrenheitItem.setVisible(true);
        } else {
            celciusItem.setVisible(true);
            fahrenheitItem.setVisible(false);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(LayoutInflater.from(getActivity()));
        locationViewModel = ViewModelProviders.of(getActivity()).get(LocationViewModel.class);
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        locationViewModel.getDeviceCurrentLocation();

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_weather, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                currentnLocation = location;
//                binding.latLngTV.setText(location.getLatitude() + "," + location.getLongitude());
                initializeCurrentWeather(location);
                initializeForecastWeather(location);
                try {
                    convertLatLngToStreetAddress(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        weatherViewModel.currentResponseLD.observe(this, new Observer<CurrentWeatherResponseBody>() {
//            @Override
//            public void onChanged(CurrentWeatherResponseBody responseBody) {
//                double temp = responseBody.getMain().getTemp();
//                long date = responseBody.getDt();
//                String city = responseBody.getName();
//                String icon = responseBody.getWeather().get(0).getIcon();
//                Picasso.get().load(EventUtils.WEATHER_CONDITION_ICON_PREFIX+icon).into(binding.weatherIcon);
//                binding.latLngTV.setText(temp+"\n"+date+"\n"+city);
//            }
//        });
    }

    private void initializeForecastWeather(Location location) {
        String apikey = getString(R.string.forecast_api_key);
        weatherViewModel.getForecast(location, unit, apikey).observe(this, new Observer<ForecastWeatherResponseBody>() {
            @Override
            public void onChanged(ForecastWeatherResponseBody responseBody) {
                List<ForecastList> forecastLists = responseBody.getList();
                String temp = "";
                for (ForecastList fl : forecastLists) {
                    temp += "Max: " + Math.round(fl.getTemp().getMax()) + EventUtils.DEGREE + tempUnit + ", " + " Min: " + Math.round(fl.getTemp().getMin()) + EventUtils.DEGREE + tempUnit + "\n";
                }
                binding.forecastTempTV.setText(temp);
            }
        });
    }

    private void initializeCurrentWeather(Location location) {
        String apikey = getString(R.string.weather_api_key);
        weatherViewModel.getCurrentWeather(location, unit, apikey).observe(this, new Observer<CurrentWeatherResponseBody>() {
            @Override
            public void onChanged(CurrentWeatherResponseBody responseBody) {
                double temp = responseBody.getMain().getTemp();
//                long date = responseBody.getDt();
                String date = EventUtils.getFormattedDate(responseBody.getDt());
                String city = responseBody.getName();
                String icon = responseBody.getWeather().get(0).getIcon();
                Picasso.get().load(EventUtils.WEATHER_CONDITION_ICON_PREFIX+icon + ".png").into(binding.weatherIcon);
                binding.latLngTV.setText(Math.round(temp) + EventUtils.DEGREE + tempUnit + "\n"+date+"\n"+city);
            }
        });
    }

    private void convertLatLngToStreetAddress(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity());
//        List<Address> addresses = geocoder.getFromLocationName("Chittagong", 1);
        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        String address = addressList.get(0).getAddressLine(0);
        binding.addressTV.setText(address);
    }
}