package com.wungatech.openweather;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wungatech.openweather.Adapters.ForecastAdapter;
import com.wungatech.openweather.Models.ForecastResponse;
import com.wungatech.openweather.Services.WeatherService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ForecastFragment extends Fragment {

    String latitude;
    String longtitude;

    WeatherService weatherService;
    RecyclerView recyclerView;
    public ForecastFragment() {
    }

    public ForecastFragment(String lat, String lon){
        latitude = lat;
        longtitude = lon;
    }

    public static ForecastFragment newInstance(String lat, String lon) {
        ForecastFragment fragment = new ForecastFragment(lat, lon);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        recyclerView = view.findViewById(R.id.dayForecast);
        weatherService = new WeatherService();

        try {
            weatherService.getForecastWeather(latitude, longtitude).thenAccept(forecastResponse -> {
                getActivity().runOnUiThread(() -> {
                    if (forecastResponse != null) {
                        List<ForecastResponse.ForecastItem> forecastItems = forecastResponse.list;
                        List<ForecastResponse.ForecastItem> filteredList = filterForecastForNext5Days(forecastItems);
                        Log.e("list size", String.valueOf(filteredList.size()));
                        ForecastAdapter adapter = new ForecastAdapter(filteredList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });
            }).exceptionally(e -> {
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return view;
    }
    //utility method that filters for the next 5 days weather forecast because api returns 3 hour intervals
    private static List<ForecastResponse.ForecastItem> filterForecastForNext5Days(List<ForecastResponse.ForecastItem> forecastItems) {
        List<ForecastResponse.ForecastItem> filteredList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Set to keep track of unique dates
        Set<String> uniqueDates = new HashSet<>();

        // Iterate through forecast items
        for (ForecastResponse.ForecastItem item : forecastItems) {
            try {
                Date itemDate = dateFormat.parse(item.dt_txt.split(" ")[0]); // Extracting only the date part

                // Check if the item date is at least today
                if (itemDate.after(new Date()) || isSameDay(new Date(), itemDate)) {
                    // Check if the item date is not already in the set
                    if (uniqueDates.add(dateFormat.format(itemDate))) {
                        filteredList.add(item);
                    }

                    // Check if we have collected data for the next 5 days
                    if (filteredList.size() == 5) {
                        break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filteredList;
    }

    // Helper method to check if a list already contains an item for a specific date
    private static boolean containsDate(List<ForecastResponse.ForecastItem> list, Date date) {
        for (ForecastResponse.ForecastItem item : list) {
            try {
                Date itemDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(item.dt_txt);
                if (isSameDay(itemDate, date)) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Helper method to check if two dates are on the same day
    private static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

}