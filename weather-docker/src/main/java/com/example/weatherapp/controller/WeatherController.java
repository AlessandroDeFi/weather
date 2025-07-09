package com.example.weatherapp.controller;

import com.example.weatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/")
    public String getWeatherForecast(Model model) {
        model.addAttribute("forecast", weatherService.getWeeklyForecast());
        return "weather";
    }
}