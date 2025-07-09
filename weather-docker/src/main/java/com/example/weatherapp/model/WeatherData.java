package com.example.weatherapp.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WeatherData {
    private LocalDate date;
    private String description;
    private double temperature;
    private int humidity;
    private double windSpeed;
    
    public WeatherData(LocalDate date, String description, double temperature, int humidity, double windSpeed) {
        this.date = date;
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
}