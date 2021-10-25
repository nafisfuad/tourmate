package com.nafisfuad.firebasemad16;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.nafisfuad.firebasemad16.viewmodels.LocationViewModel;

public class MainActivity extends AppCompatActivity {
    private LocationViewModel locationViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isLocationPermissionGranted();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isLocationPermissionGranted() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationViewModel.getDeviceCurrentLocation();
        }
    }
}