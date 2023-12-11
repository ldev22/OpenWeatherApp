package com.wungatech.openweather.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    @SerializedName("cod")
    public String cod;
    @SerializedName("message")
    public int message;
    @SerializedName("cnt")
    public int cnt;
    @SerializedName("list")
    public List<ForecastItem> list;
    @SerializedName("city")
    public City city;

    public List<ForecastItem> getList() {
        return list;
    }

    public static class ForecastItem {
        @SerializedName("dt")
        public long dt;
        @SerializedName("main")
        public Main main;
        @SerializedName("weather")
        public List<Weather> weather;
        @SerializedName("clouds")
        public Clouds clouds;
        @SerializedName("wind")
        public Wind wind;
        @SerializedName("visibility")
        public int visibility;
        @SerializedName("pop")
        public float pop;
        @SerializedName("sys")
        public Sys sys;
        @SerializedName("dt_txt")
        public String dt_txt;

        public static class Main {
            @SerializedName("temp")
            public float temp;
            @SerializedName("feels_like")
            public float feels_like;
            @SerializedName("temp_min")
            public float temp_min;
            @SerializedName("temp_max")
            public float temp_max;
            @SerializedName("pressure")
            public int pressure;
            @SerializedName("sea_level")
            public int sea_level;
            @SerializedName("grnd_level")
            public int grnd_level;
            @SerializedName("humidity")
            public int humidity;
            @SerializedName("temp_kf")
            public float temp_kf;
        }

        public static class Weather {
            @SerializedName("id")
            public int id;
            @SerializedName("main")
            public String main;
            @SerializedName("description")
            public String description;
            @SerializedName("icon")
            public String icon;
        }

        public static class Clouds {
            @SerializedName("all")
            public int all;
        }

        public static class Wind {
            @SerializedName("speed")
            public float speed;
            @SerializedName("deg")
            public int deg;
            @SerializedName("gust")
            public float gust;
        }

        public static class Sys {
            @SerializedName("pod")
            public String pod;
        }
    }

    public static class City {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("coord")
        public Coord coord;
        @SerializedName("country")
        public String country;
        @SerializedName("population")
        public int population;
        @SerializedName("timezone")
        public int timezone;
        @SerializedName("sunrise")
        public long sunrise;
        @SerializedName("sunset")
        public long sunset;
    }

    public static class Coord {
        @SerializedName("lat")
        public double lat;
        @SerializedName("lon")
        public double lon;
    }
}
