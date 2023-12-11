package com.wungatech.openweather.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {
    @SerializedName("dt")
    public long timestamp;

    @SerializedName("temp")
    public float temperature;

    @SerializedName("feels_like")
    public float feelsLike;

    @SerializedName("pressure")
    public int pressure;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("dew_point")
    public double dewPoint;

    @SerializedName("uvi")
    public double uvIndex;

    @SerializedName("clouds")
    public int cloudiness;

    @SerializedName("visibility")
    public int visibility;

    @SerializedName("wind_speed")
    public double windSpeed;

    @SerializedName("wind_deg")
    public int windDegree;

    @SerializedName("wind_gust")
    public double windGust;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("pop")
    public double precipitationProbability;

    @SerializedName("rain")
    public Rain rain;
}

