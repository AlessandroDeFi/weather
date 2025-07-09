package com.example.weatherapp.service;

import com.example.weatherapp.model.WeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WeatherService {
    private final String[] weatherDescriptions = {
        "Soleggiato", "Nuvoloso", "Parzialmente nuvoloso", "Piovoso", "Temporale"
    };
    
    public List<WeatherData> getWeeklyForecast() {
        List<WeatherData> forecast = new ArrayList<>();
        Random random = new Random();
        LocalDate today = LocalDate.now();
        
        for (int i = 0; i < 7; i++) {
            String description = weatherDescriptions[random.nextInt(weatherDescriptions.length)];
            double temperature = 15 + random.nextDouble() * 15; // Temperatura tra 15 e 30 gradi
            int humidity = 40 + random.nextInt(41); // Umidità tra 40% e 80%
            double windSpeed = random.nextDouble() * 20; // Velocità del vento tra 0 e 20 km/h
            
            forecast.add(new WeatherData(
                today.plusDays(i),
                description,
                Math.round(temperature * 10.0) / 10.0,
                humidity,
                Math.round(windSpeed * 10.0) / 10.0
            ));
        }
        
        return forecast;
    }
}