package com.wungatech.openweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wungatech.openweather.Models.WeatherData;
import com.wungatech.openweather.Services.WeatherService;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {
    String latitude;
    String longtitude;
    WeatherService weatherService;
    GoogleMap gMap;
    public MapsFragment() {
        // Required empty public constructor
    }

    public MapsFragment(String lat, String lon){
        latitude = lat;
        longtitude = lon;
    }

    public static MapsFragment newInstance(String lat, String lon) {
        MapsFragment fragment = new MapsFragment(lat, lon);
        Bundle args = new Bundle();
        return fragment;
    }
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            Double lat = Double.parseDouble(latitude);
            Double lon = Double.parseDouble(longtitude);
            LatLng town = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(town).title("Marker"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(town));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        weatherService = new WeatherService();
        try {
            weatherService.getHourlyWeather(latitude, longtitude).thenAccept(weatherResponse -> {
                getActivity().runOnUiThread(() -> {
                    if (weatherResponse != null) {
                        List<WeatherData> hourlyData = weatherResponse.hourlyData;
                        // Loop through the hourly data to retrieve precipitation information
                        for (WeatherData data : hourlyData) {
                            double precipitation = data.precipitationProbability;

                            // Add a marker or overlay on the map based on precipitation intensity or type
                            // For example, you can add a marker at the location with a different icon/color based on precipitation
                            if (precipitation > 0) {
                                Double lat = Double.parseDouble(latitude);
                                Double lon = Double.parseDouble(longtitude);
                                LatLng location = new LatLng(lat, lon);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(location)
                                        .title("Precipitation")
                                        .snippet("Intensity: " + precipitation);
                                gMap.addMarker(markerOptions);
                            }
                        }
                    }
                });
            }).exceptionally(e -> {
                // Handle exception if needed
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}