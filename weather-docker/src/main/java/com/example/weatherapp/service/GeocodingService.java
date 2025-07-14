package com.example.weatherapp.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeocodingService {

    private final Map<String, Coordinates> cityCoordinates;

    public GeocodingService() {
        cityCoordinates = new HashMap<>();
        // Coordinate delle principali città italiane
        cityCoordinates.put("Roma", new Coordinates(41.8919, 12.5113));
        cityCoordinates.put("Milano", new Coordinates(45.4642, 9.1900));
        cityCoordinates.put("Napoli", new Coordinates(40.8518, 14.2681));
        cityCoordinates.put("Torino", new Coordinates(45.0703, 7.6869));
        cityCoordinates.put("Palermo", new Coordinates(38.1157, 13.3615));
        cityCoordinates.put("Genova", new Coordinates(44.4056, 8.9463));
        cityCoordinates.put("Bologna", new Coordinates(44.4949, 11.3426));
        cityCoordinates.put("Firenze", new Coordinates(43.7696, 11.2558));
        cityCoordinates.put("Bari", new Coordinates(41.1171, 16.8719));
        cityCoordinates.put("Catania", new Coordinates(37.5079, 15.0830));
    }

    public Coordinates getCoordinates(String city) {
        // Normalizza il nome della città (prima lettera maiuscola)
        city = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
        return cityCoordinates.getOrDefault(city, cityCoordinates.get("Roma")); // Roma come default
    }

    public static class Coordinates {
        private final double latitude;
        private final double longitude;

        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}