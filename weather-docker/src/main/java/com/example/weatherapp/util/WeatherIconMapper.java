package com.example.weatherapp.util;

import org.springframework.stereotype.Component;

@Component
public class WeatherIconMapper {

    /**
     * Mappa la descrizione del meteo a un'icona CSS (Font Awesome)
     * 
     * @param description La descrizione del meteo in italiano
     * @return Il nome dell'icona Font Awesome
     */
    public String getIconClass(String description) {
        String lowerDesc = description.toLowerCase();
        
        if (lowerDesc.contains("soleggiato") || lowerDesc.contains("sereno")) {
            return "fas fa-sun";
        } else if (lowerDesc.contains("nuvoloso") || lowerDesc.contains("nuvole")) {
            return "fas fa-cloud";
        } else if (lowerDesc.contains("parzialmente") || lowerDesc.contains("sparse")) {
            return "fas fa-cloud-sun";
        } else if (lowerDesc.contains("pioggia") || lowerDesc.contains("piovoso")) {
            return "fas fa-cloud-rain";
        } else if (lowerDesc.contains("temporale")) {
            return "fas fa-bolt";
        } else if (lowerDesc.contains("neve") || lowerDesc.contains("nevica")) {
            return "fas fa-snowflake";
        } else if (lowerDesc.contains("nebbia") || lowerDesc.contains("foschia")) {
            return "fas fa-smog";
        } else if (lowerDesc.contains("vento") || lowerDesc.contains("ventoso")) {
            return "fas fa-wind";
        } else {
            return "fas fa-cloud-sun"; // Icona predefinita
        }
    }
}