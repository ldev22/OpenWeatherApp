package com.wungatech.openweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.wungatech.openweather.Services.WeatherService;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    TextView mainWeatherLocation;
    TextView currentTemp;

    ImageView weatherIcon;
    TextView mainWeather;
    ProgressBar splash;
    LinearLayout information;
    LocationManager locationManager;
    LocationListener locationListener;
    public static String lat;
    public static String lon;
    WeatherService weatherService;
    RelativeLayout error;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherService = new WeatherService();
        mainWeatherLocation = findViewById(R.id.mainWeatherLocation);
        currentTemp = findViewById(R.id.mainWeatherTemp);
        weatherIcon = findViewById(R.id.mainWeatherConditionIc);
        mainWeather = findViewById(R.id.mainWeather);
        splash = findViewById(R.id.splash);
        information = findViewById(R.id.weatherInformation);
        Button settingsButton = findViewById(R.id.settings);
        error = findViewById(R.id.error_screen);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(settingsIntent);
            }
        });

        if (isNetworkConnected()) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            //final Location[] currentLocation = new Location[1];
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Request location updates
                if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestLocationUpdates();
                } else {
                    // Request location permission using EasyPermissions
                    EasyPermissions.requestPermissions(
                            new PermissionRequest.Builder(this, 1, Manifest.permission.ACCESS_FINE_LOCATION)
                                    .setRationale("Location permission is required for this app to function properly.")
                                    .setPositiveButtonText("Allow")
                                    .setNegativeButtonText("Deny")
                                    .build());
                }

            } else {
                splash.setVisibility(View.GONE);
                information.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        } else {
            splash.setVisibility(View.GONE);
            information.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }

    private void checkLocationPermission() {
        PermissionDialog permissionDialog = new PermissionDialog(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                R.string.dialog_message,
                "Grant",
                "Deny",
                isGranted -> {
                    if (isGranted) {
                        return;
                    } else {
                        return;
                    }
                });

        permissionDialog.show();
    }

    private void requestLocationUpdates() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            initiateLocationUpdates();
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, 1, Manifest.permission.ACCESS_FINE_LOCATION)
                            .setRationale("Location permission is required for this app to function properly.")
                            .setPositiveButtonText("Allow")
                            .setNegativeButtonText("Deny")
                            .build());
        }
    }

    private void initiateLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        weatherService.getCurrentWeather(lat, lon).thenAccept(weatherResponse -> {
                            runOnUiThread(() -> {
                                mainWeatherLocation.setText(weatherResponse.name);
                                currentTemp.setText(String.valueOf(weatherResponse.main.temp) + "\u00B0");
                                String iconUrl = "https://openweathermap.org/img/wn/" + weatherResponse.weather.get(0).icon + "@2x.png";
                                Picasso.get().load(iconUrl).into(weatherIcon);
                                mainWeather.setText(weatherResponse.weather.get(0).main + " " + weatherResponse.main.temp_max + "\u00B0 / " + weatherResponse.main.temp_min + "\u00B0");
                                ForecastFragment forecastFragment = ForecastFragment.newInstance(lat, lon);
                                //please note using the maps fragment will require you to have google maps key
                                MapsFragment mapsFragment = MapsFragment.newInstance(lat, lon);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.weather5dayForecast, forecastFragment);
                                fragmentTransaction.replace(R.id.weatherMap, mapsFragment);
                                fragmentTransaction.commit();
                                splash.setVisibility(View.GONE);
                                information.setVisibility(View.VISIBLE);
                            });
                        }).exceptionally(e -> {
                            return null;
                        });
                    }
                } catch (IOException e) {
                    splash.setVisibility(View.GONE);
                    information.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                    throw new RuntimeException(e);
                }

                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Location permission granted, proceed with location-related tasks
        if (requestCode == 1) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Location permission denied, handle the case (e.g., show a message to the user)
        if (requestCode == 1) {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            splash.setVisibility(View.GONE);
            information.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }
}
