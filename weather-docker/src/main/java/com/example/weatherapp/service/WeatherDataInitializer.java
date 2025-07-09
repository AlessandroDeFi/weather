package com.example.weatherapp.service;

import com.example.weatherapp.entity.WeatherEntity;
import com.example.weatherapp.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class WeatherDataInitializer implements CommandLineRunner {

    @Autowired
    private WeatherRepository weatherRepository;
    
    @Value("${weather.data.cities}")
    private String citiesString;
    
    @Value("${weather.data.descriptions}")
    private String descriptionsString;
    
    private final Random random = new Random();
    
    @Override
    public void run(String... args) {
        // Converti le stringhe di configurazione in liste
        List<String> cities = Arrays.asList(citiesString.split(","));
        List<String> descriptions = Arrays.asList(descriptionsString.split(","));
        
        // Genera dati meteo per ogni città
        LocalDate today = LocalDate.now();
        
        for (String city : cities) {
            // Elimina eventuali dati esistenti per la città
            weatherRepository.deleteByCityIgnoreCase(city);
            
            // Genera previsioni per i prossimi 7 giorni
            for (int i = 0; i < 7; i++) {
                LocalDate date = today.plusDays(i);
                String description = descriptions.get(random.nextInt(descriptions.size()));
                
                // Genera valori casuali realistici
                double temperature = 10.0 + random.nextDouble() * 25.0; // Tra 10 e 35 gradi
                int humidity = 30 + random.nextInt(60); // Tra 30% e 90%
                double windSpeed = 1.0 + random.nextDouble() * 19.0; // Tra 1 e 20 km/h
                
                // Arrotonda i valori per una migliore leggibilità
                temperature = Math.round(temperature * 10.0) / 10.0;
                windSpeed = Math.round(windSpeed * 10.0) / 10.0;
                
                // Crea e salva l'entità
                WeatherEntity weatherEntity = new WeatherEntity(
                    city,
                    date,
                    description,
                    temperature,
                    humidity,
                    windSpeed
                );
                
                weatherRepository.save(weatherEntity);
            }
        }
        
        System.out.println("Database inizializzato con dati meteo per " + cities.size() + " città");
    }
}