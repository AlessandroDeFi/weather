package com.example.weatherapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "weather_data")
@Data
@NoArgsConstructor
public class WeatherEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String city;
    private LocalDate date;
    private String description;
    private double temperature;
    private int humidity;
    private double windSpeed;
    
    public WeatherEntity(String city, LocalDate date, String description, double temperature, int humidity, double windSpeed) {
        this.city = city;
        this.date = date;
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
}