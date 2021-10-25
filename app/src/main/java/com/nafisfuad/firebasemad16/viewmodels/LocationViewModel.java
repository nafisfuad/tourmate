package com.nafisfuad.firebasemad16.viewmodels;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationViewModel extends AndroidViewModel {
    private FusedLocationProviderClient providerClient;
    private Context context;
    public MutableLiveData<Location> locationLD = new MutableLiveData<>();
    public LocationViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        providerClient = LocationServices.getFusedLocationProviderClient(context);
//        getDeviceCurrentLocation();
    }

    public void getDeviceCurrentLocation() {
//        providerClient.removeLocationUpdates() for updated location
        providerClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    return;
                }
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
                locationLD.postValue(location);
            }

        });
    }
}
