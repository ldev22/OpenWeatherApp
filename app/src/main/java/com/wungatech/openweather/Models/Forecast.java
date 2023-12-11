package com.wungatech.openweather.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast    {
    @SerializedName("dt")
    public int dt;
    @SerializedName("main")
    public Main main;
    @SerializedName("weather")
    public List<Weather> weather;
    @SerializedName("visibility")
    public int visibility;
    @SerializedName("pop")
    public double pop;
    @SerializedName("dt_txt")
    public String dt_txt;
}