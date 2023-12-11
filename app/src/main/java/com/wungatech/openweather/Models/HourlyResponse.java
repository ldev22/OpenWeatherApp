package com.wungatech.openweather.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyResponse {
    @SerializedName("lat")
    public double latitude;

    @SerializedName("lon")
    public double longitude;

    @SerializedName("timezone")
    public String timezone;

    @SerializedName("timezone_offset")
    public int timezoneOffset;

    @SerializedName("hourly")
    public List<WeatherData> hourlyData;


}
