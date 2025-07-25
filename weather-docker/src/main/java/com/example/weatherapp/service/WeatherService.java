package com.example.weatherapp.service;

import com.example.weatherapp.entity.WeatherEntity;
import com.example.weatherapp.model.WeatherData;
import com.example.weatherapp.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    
    @Autowired
    private WeatherRepository weatherRepository;
    
    @Autowired
    private OpenMeteoService openMeteoService;
    
    @Autowired
    private GeocodingService geocodingService;
    
    @Value("${weather.data.descriptions}")
    private String descriptionsString;
    
    private final Random random = new Random();
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Ottiene le previsioni meteo per Roma (città predefinita)
     * @return lista di previsioni meteo per i prossimi 7 giorni
     */
    public List<WeatherData> getWeeklyForecast() {
        return getWeeklyForecast("Rome");
    }
    
    /**
     * Ottiene le previsioni meteo per una città specifica
     * @param city nome della città
     * @return lista di previsioni meteo per i prossimi 7 giorni
     */
    public List<WeatherData> getWeeklyForecast(String city) {
        try {
            // Estrai solo il nome della città se è nel formato "città,paese"
            if (city.contains(",")) {
                city = city.split(",")[0];
            }
            
            // Capitalizza la prima lettera della città per uniformità
            city = capitalizeFirstLetter(city);
            
            // Ottieni le coordinate della città
            GeocodingService.Coordinates coords = geocodingService.getCoordinates(city);
            
            // Chiama l'API di Open Meteo
            OpenMeteoService.WeatherApiResponse response = openMeteoService.getWeatherForecast(
                coords.getLatitude(), 
                coords.getLongitude()
            );
            
            if (response != null && response.getDaily() != null) {
                List<WeatherData> forecast = new ArrayList<>();
                List<String> descriptions = Arrays.asList(descriptionsString.split(","));
                Random random = new Random();
                
                for (int i = 0; i < response.getDaily().getTime().size(); i++) {
                    LocalDate date = LocalDate.parse(response.getDaily().getTime().get(i), dateFormatter);
                    String description = descriptions.get(random.nextInt(descriptions.size()));
                    
                    WeatherData weatherData = new WeatherData(
                        date,
                        description,
                        response.getDaily().getTemperatures().get(i),
                        response.getDaily().getHumidity().get(i),
                        response.getDaily().getWindSpeed().get(i)
                    );
                    
                    forecast.add(weatherData);
                    
                    // Salva nel database
                    WeatherEntity entity = new WeatherEntity(
                        city,
                        date,
                        description,
                        response.getDaily().getTemperatures().get(i),
                        response.getDaily().getHumidity().get(i),
                        response.getDaily().getWindSpeed().get(i)
                    );
                    weatherRepository.save(entity);
                }
                
                return forecast;
            }
            
            // Se la chiamata API fallisce, usa i dati dal database se disponibili
            List<WeatherEntity> weatherEntities = weatherRepository.findByCityAndDateGreaterThanEqualOrderByDateAsc(
                city, 
                LocalDate.now()
            );
            
            if (!weatherEntities.isEmpty()) {
                return mapToWeatherData(weatherEntities);
            }
            
            // Se non ci sono dati nel database, genera dati casuali
            return generateRandomForecast(city);
            
        } catch (Exception e) {
            // In caso di errore, genera dati casuali
            return getFallbackForecast(city);
        }
    }
    
    /**
     * Capitalizza la prima lettera di una stringa
     * @param input stringa di input
     * @return stringa con la prima lettera maiuscola
     */
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    
    /**
     * Converte le entità dal database in oggetti WeatherData
     * @param weatherEntities lista di entità dal database
     * @return lista di oggetti WeatherData
     */
    private List<WeatherData> mapToWeatherData(List<WeatherEntity> weatherEntities) {
        List<WeatherData> forecast = new ArrayList<>();
        
        for (WeatherEntity entity : weatherEntities) {
            forecast.add(new WeatherData(
                entity.getDate(),
                entity.getDescription(),
                entity.getTemperature(),
                entity.getHumidity(),
                entity.getWindSpeed()
            ));
        }
        
        return forecast;
    }
    
    /**
     * Genera previsioni meteo casuali per una città e le salva nel database
     * @param city nome della città
     * @return lista di previsioni meteo generate
     */
    private List<WeatherData> generateRandomForecast(String city) {
        // Capitalizza la prima lettera della città per uniformità
        city = capitalizeFirstLetter(city);
        
        List<String> descriptions = Arrays.asList(descriptionsString.split(","));
        List<WeatherData> forecast = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        // Elimina eventuali dati esistenti per la città
        weatherRepository.deleteByCityIgnoreCase(city);
        
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
            
            // Crea e salva l'entità nel database
            WeatherEntity weatherEntity = new WeatherEntity(
                city,
                date,
                description,
                temperature,
                humidity,
                windSpeed
            );
            
            weatherRepository.save(weatherEntity);
            
            // Aggiungi alla lista di previsioni
            forecast.add(new WeatherData(
                date,
                description,
                temperature,
                humidity,
                windSpeed
            ));
        }
        
        return forecast;
    }
    
    /**
     * Genera previsioni meteo di fallback in caso di errori
     * @param city nome della città
     * @return lista di previsioni meteo di fallback
     */
    private List<WeatherData> getFallbackForecast(String city) {
        // Estrai solo il nome della città se è nel formato "città,paese"
        if (city.contains(",")) {
            city = city.split(",")[0];
        }
        
        // Capitalizza la prima lettera della città per uniformità
        city = capitalizeFirstLetter(city);
        
        // Genera dati di fallback
        List<WeatherData> forecast = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<String> descriptions = Arrays.asList(descriptionsString.split(","));
        
        for (int i = 0; i < 7; i++) {
            String description = "Dati non disponibili per " + city + " - " + 
                                descriptions.get(i % descriptions.size());
            
            forecast.add(new WeatherData(
                today.plusDays(i),
                description,
                20.0,
                50,
                10.0
            ));
        }
        
        return forecast;
    }
    
    /**
     * Ottiene la lista di tutte le città disponibili nel database
     * @return lista di nomi di città
     */
    public List<String> getAvailableCities() {
        return weatherRepository.findDistinctCity();
    }
}