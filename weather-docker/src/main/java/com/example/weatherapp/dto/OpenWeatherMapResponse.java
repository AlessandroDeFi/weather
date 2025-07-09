package com.example.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherMapResponse {
    private String cod;
    private int message;
    private int cnt;
    private List<WeatherForecast> list;
    private City city;
    
    @Data
    public static class WeatherForecast {
        private long dt;
        private Main main;
        private List<Weather> weather;
        private Clouds clouds;
        private Wind wind;
        private int visibility;
        private double pop;
        private Sys sys;
        @JsonProperty("dt_txt")
        private String dtTxt;
    }
    
    @Data
    public static class Main {
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        @JsonProperty("temp_min")
        private double tempMin;
        @JsonProperty("temp_max")
        private double tempMax;
        private int pressure;
        @JsonProperty("sea_level")
        private int seaLevel;
        @JsonProperty("grnd_level")
        private int grndLevel;
        private int humidity;
        @JsonProperty("temp_kf")
        private double tempKf;
    }
    
    @Data
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }
    
    @Data
    public static class Clouds {
        private int all;
    }
    
    @Data
    public static class Wind {
        private double speed;
        private int deg;
        private double gust;
    }
    
    @Data
    public static class Sys {
        private String pod;
    }
    
    @Data
    public static class City {
        private int id;
        private String name;
        private Coord coord;
        private String country;
        private int population;
        private int timezone;
        private long sunrise;
        private long sunset;
    }
    
    @Data
    public static class Coord {
        private double lat;
        private double lon;
    }
}