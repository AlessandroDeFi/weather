package com.example.weatherapp.controller;

import com.example.weatherapp.service.WeatherService;
import com.example.weatherapp.util.WeatherIconMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private WeatherIconMapper weatherIconMapper;
    
    /**
     * Endpoint principale per visualizzare le previsioni meteo
     * @param city città per cui visualizzare le previsioni
     * @param model modello per la vista
     * @return nome della vista da renderizzare
     */
    @GetMapping("/")
    public String getWeatherForecast(
            @RequestParam(name = "city", required = false, defaultValue = "Rome") String city,
            @RequestParam(name = "newCity", required = false) String newCity,
            Model model) {
        try {
            // Determina quale città usare (newCity ha priorità su city)
            String cityToUse;
            if (newCity != null && !newCity.trim().isEmpty()) {
                cityToUse = newCity.trim();
            } else {
                cityToUse = city;
            }
            
            // Ottieni le previsioni meteo
            model.addAttribute("forecast", weatherService.getWeeklyForecast(cityToUse));
            
            // Estrai solo il nome della città se è nel formato "città,paese"
            String cityName = cityToUse;
            if (cityToUse.contains(",")) {
                cityName = cityToUse.split(",")[0];
            }
            
            // Capitalizza la prima lettera della città per uniformità nella visualizzazione
            if (cityName != null && !cityName.isEmpty()) {
                cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1).toLowerCase();
            }
            
            model.addAttribute("city", cityName);
            
            // Aggiungi la lista di città disponibili
            model.addAttribute("availableCities", weatherService.getAvailableCities());
            
            // Nessun errore
            model.addAttribute("error", false);
            model.addAttribute("iconMapper", weatherIconMapper);
        } catch (Exception e) {
            // In caso di errore, mostra i dati per Roma
            model.addAttribute("forecast", weatherService.getWeeklyForecast("Rome"));
            model.addAttribute("city", "Roma");
            model.addAttribute("availableCities", weatherService.getAvailableCities());
            model.addAttribute("error", true);
            
            // Determina quale città ha causato l'errore
            String errorCity = (newCity != null && !newCity.trim().isEmpty()) ? newCity : city;
            
            // Capitalizza la prima lettera della città per uniformità nel messaggio di errore
            if (errorCity != null && !errorCity.isEmpty()) {
                if (errorCity.contains(",")) {
                    errorCity = errorCity.split(",")[0];
                }
                errorCity = errorCity.substring(0, 1).toUpperCase() + errorCity.substring(1).toLowerCase();
            }
            
            model.addAttribute("errorMessage", "Errore nel recupero dei dati meteo per " + errorCity + ". Mostrati dati per Roma.");
            model.addAttribute("iconMapper", weatherIconMapper);
        }
        return "weather";
    }
    
    /**
     * API REST per ottenere la lista di città disponibili
     * @return lista di nomi di città in formato JSON
     */
    @GetMapping("/api/cities")
    @ResponseBody
    public List<String> getAvailableCities() {
        return weatherService.getAvailableCities();
    }
}