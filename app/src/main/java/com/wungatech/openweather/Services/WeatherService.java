package com.wungatech.openweather.Services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.wungatech.openweather.Models.ForecastResponse;
import com.wungatech.openweather.Models.HourlyResponse;
import com.wungatech.openweather.Models.WeatherResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService {
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "651e65c83907e8834259263057d957f5";
    private static Retrofit retrofit;
    private static WeatherServiceAPI service;

    public WeatherService() {
        // Create an OkHttpClient with a logging interceptor
        OkHttpClient okHttpClient = createOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)  // Set the OkHttpClient
                .build();

        service = retrofit.create(WeatherServiceAPI.class);
    }

    private OkHttpClient createOkHttpClient() {
        // Create a logging interceptor to track if the response is working
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Build the OkHttpClient
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    public CompletableFuture<WeatherResponse> getCurrentWeather(String lat, String lon) throws IOException {
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, AppId, "metric", "6");
        CompletableFuture<WeatherResponse> future = new CompletableFuture<>();
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200 || response.isSuccessful()) {
                    Log.e("Response", response.raw().toString()); // Print the raw response to the log
                    Log.e("Response Body", response.body().toString()); // Print the response body to the log

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        future.complete(response.body());
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        future.completeExceptionally(new IOException("API call failed with response code: " + response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Call Failed again", t.getMessage());
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public CompletableFuture<ForecastResponse> getForecastWeather(String lat, String lon) throws IOException {
        Call<ForecastResponse> call = service.getForecastWeatherData(lat, lon, AppId, "metric");
        CompletableFuture<ForecastResponse> future = new CompletableFuture<>();
        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                if (response.code() == 200 || response.isSuccessful()) {
                    Log.e("Response", response.raw().toString()); // Print the raw response to the log
                    Log.e("Response Body", response.body().toString()); // Print the response body to the log

                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new IOException("API call failed with response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e("API Call Failed again", t.getMessage());
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public CompletableFuture<HourlyResponse> getHourlyWeather(String lat, String lon) throws IOException {
        Call<HourlyResponse> call = service.getHourForecastData(lat, lon, AppId, "metric");
        CompletableFuture<HourlyResponse> future = new CompletableFuture<>();
        call.enqueue(new Callback<HourlyResponse>() {
            @Override
            public void onResponse(@NonNull Call<HourlyResponse> call, @NonNull Response<HourlyResponse> response) {
                if (response.code() == 200 || response.isSuccessful()) {
                    Log.e("Response", response.raw().toString()); // Print the raw response to the log
                    Log.e("Response Body", response.body().toString()); // Print the response body to the log

                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new IOException("API call failed with response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<HourlyResponse> call, Throwable t) {
                Log.e("API Call Failed again", t.getMessage());
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
