package com.example.weatherapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenMeteoService {

    private static final Logger logger = LoggerFactory.getLogger(OpenMeteoService.class);
    private static final String API_URL = "https://api.open-meteo.com/v1/forecast";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.api.retry.max-attempts}")
    private int maxAttempts;

    @Value("${weather.api.retry.delay}")
    private long retryDelay;

    @Retryable(value = RestClientException.class,
              maxAttempts = 3,
              backoff = @Backoff(delay = 1000))
    public WeatherApiResponse getWeatherForecast(double latitude, double longitude) {
        logger.info("Chiamata API Open Meteo per coordinate: lat={}, lon={}", latitude, longitude);
        String url = String.format("%s?latitude=%.4f&longitude=%.4f&daily=temperature_2m_max,relative_humidity_2m,wind_speed_10m_max&timezone=auto",
                API_URL, latitude, longitude);
        return restTemplate.getForObject(url, WeatherApiResponse.class);
    }

    public static class WeatherApiResponse {
        private Daily daily;

        public Daily getDaily() {
            return daily;
        }

        public void setDaily(Daily daily) {
            this.daily = daily;
        }
    }

    public static class Daily {
        private List<String> time;
        
        @JsonProperty("temperature_2m_max")
        private List<Double> temperatures;
        
        @JsonProperty("relative_humidity_2m")
        private List<Integer> humidity;
        
        @JsonProperty("wind_speed_10m_max")
        private List<Double> windSpeed;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Double> getTemperatures() {
            return temperatures;
        }

        public void setTemperatures(List<Double> temperatures) {
            this.temperatures = temperatures;
        }

        public List<Integer> getHumidity() {
            return humidity;
        }

        public void setHumidity(List<Integer> humidity) {
            this.humidity = humidity;
        }

        public List<Double> getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(List<Double> windSpeed) {
            this.windSpeed = windSpeed;
        }
    }
}