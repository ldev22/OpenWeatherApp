package com.wungatech.openweather.Models;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("1h")
    private double oneHour;

    @SerializedName("3h")
    private double threeHours;

    public double getOneHour() {
        return oneHour;
    }

    public void setOneHour(double oneHour) {
        this.oneHour = oneHour;
    }

    public double getThreeHours() {
        return threeHours;
    }

    public void setThreeHours(double threeHours) {
        this.threeHours = threeHours;
    }
}