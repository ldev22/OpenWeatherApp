package com.wungatech.openweather.Models;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    public double temp;
    @SerializedName("humidity")
    public int humidity;
    @SerializedName("pressure")
    public int pressure;
    @SerializedName("temp_min")
    public double temp_min;
    @SerializedName("temp_max")
    public double temp_max;
}