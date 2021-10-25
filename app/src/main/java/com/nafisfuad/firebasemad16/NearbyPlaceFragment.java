package com.nafisfuad.firebasemad16;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nafisfuad.firebasemad16.databinding.FragmentNearbyPlaceBinding;
import com.nafisfuad.firebasemad16.nearby.NearbyResponseBody;
import com.nafisfuad.firebasemad16.nearby.Result;
import com.nafisfuad.firebasemad16.viewmodels.LocationViewModel;
import com.nafisfuad.firebasemad16.viewmodels.NearbyViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NearbyPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearbyPlaceFragment extends Fragment implements OnMapReadyCallback {
//    private FragmentNearbyPlaceBinding binding;
    private LocationViewModel locationViewModel;
    private GoogleMap googleMap;
    private Button restaurantBtn, atmBtn;
    private String type = "";
    private Location myLocation = null;
    private NearbyViewModel nearbyViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NearbyPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyPlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearbyPlaceFragment newInstance(String param1, String param2) {
        NearbyPlaceFragment fragment = new NearbyPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        binding = FragmentNearbyPlaceBinding.inflate(LayoutInflater.from(getActivity()));
//        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_nearby_place, container, false);
        locationViewModel = ViewModelProviders.of(getActivity()).get(LocationViewModel.class);
        nearbyViewModel = ViewModelProviders.of(getActivity()).get(NearbyViewModel.class);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_nearby_place, container, false);
//        return binding.getRoot();
        return inflater.inflate(R.layout.fragment_nearby_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restaurantBtn = view.findViewById(R.id.btn_restaurant);
        atmBtn = view.findViewById(R.id.btn_atm);
        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
//                binding.nearbylatLngTV.setText(location.getLatitude() + ", " + location.getLongitude());
                if (location == null) {
                    return;
                }
                myLocation = location;
                LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
                if (googleMap != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 14f));
                }
            }
        });
        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.clear();
                type = "restaurant";
                getNearbyResponseData();

            }
        });
        atmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.clear();
                type = "atm";
                getNearbyResponseData();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()/*getSupportFragmentManager()*/
                .findFragmentById(R.id.nearby_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        locationViewModel.getDeviceCurrentLocation();
    }

    private void getNearbyResponseData() {
        String apiKey = getString(R.string.nearby_place_api);
        String endUrl = String.format("place/nearbysearch/json?location=%f, %f&radius=1500&type=%s&key=%s", myLocation.getLatitude(), myLocation.getLongitude(), type, apiKey);
        nearbyViewModel.getResponseFromRepo(endUrl).observe(this, new Observer<NearbyResponseBody>() {
            @Override
            public void onChanged(NearbyResponseBody nearbyResponseBody) {
                List<Result> results = nearbyResponseBody.getResults();
                for (Result r : results) {
                    double lat = r.getGeometry().getLocation().getLat();
                    double lng = r.getGeometry().getLocation().getLng();
                    String name = r.getName();
                    LatLng latLng = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().title(name).position(latLng));
                }
            }
        });

    }
}