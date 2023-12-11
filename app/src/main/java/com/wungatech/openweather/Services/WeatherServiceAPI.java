package com.wungatech.openweather.Services;

import com.wungatech.openweather.Models.ForecastResponse;
import com.wungatech.openweather.Models.HourlyResponse;
import com.wungatech.openweather.Models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceAPI {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeatherData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String appId,
            @Query("units") String units,
            @Query("cnt") String cnt
    );

    @GET("data/2.5/forecast?")
    Call<ForecastResponse> getForecastWeatherData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("APPID") String app_id,
            @Query("units") String units
    );

    @GET("data/2.5/onecall?exclude=current,minutely,daily,alerts")
    Call<HourlyResponse> getHourForecastData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("APPID") String app_id,
            @Query("units") String units
    );
}
