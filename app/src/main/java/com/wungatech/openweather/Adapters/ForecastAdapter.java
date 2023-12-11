package com.wungatech.openweather.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wungatech.openweather.Models.ForecastResponse;
import com.wungatech.openweather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private List<ForecastResponse.ForecastItem> forecastList;

    public ForecastAdapter(List<ForecastResponse.ForecastItem> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastResponse.ForecastItem forecast = forecastList.get(position);


        if (!forecast.weather.isEmpty()) {
            ForecastResponse.ForecastItem.Weather weather = forecast.weather.get(0); // Get the first weather item
            String forecastWeather = toUpper(weather.description) + " " + forecast.main.temp_max + "\u00B0" + "/" + forecast.main.temp_min + "\u00B0";
            holder.temperatureTextView.setText(forecastWeather);
            holder.dateTextView.setText(getDayOfWeek(forecast.dt_txt));
            String iconUrl = "https://openweathermap.org/img/wn/" + weather.icon + "@2x.png";
            Picasso.get().load(iconUrl).into(holder.weatherIcon);
        } else {
            // Handle the case when forecast.weather is empty
            holder.temperatureTextView.setText("");
            holder.weatherIcon.setImageResource(R.drawable.ic_launcher_background);
        }
    }


    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView temperatureTextView;
        ImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            weatherIcon = itemView.findViewById(R.id.iconWeather);
        }
    }

    private String getDayOfWeek(String weatherDay) {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Convert the weatherDay to a Calendar object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = dateFormat.parse(weatherDay);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar weatherCalendar = Calendar.getInstance();
        weatherCalendar.setTime(date);
        int weatherDayOfWeek = weatherCalendar.get(Calendar.DAY_OF_WEEK);

        // Check if the weather day is the current day
        if (weatherDayOfWeek == currentDayOfWeek) {
            return "Today";
        } else {
            // Calculate the next day
            int dayDifference = (weatherDayOfWeek - currentDayOfWeek + 7) % 7;
            int nextDay = (currentDayOfWeek + dayDifference) % 7;

            // Convert the day of the week to its three-letter abbreviation
            String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            return daysOfWeek[nextDay];
        }
    }

    private String toUpper(String input){
        String firstChar = input.substring(0, 1).toUpperCase();
        String capitalizedString = firstChar + input.substring(1);

        return capitalizedString;
    }
}


