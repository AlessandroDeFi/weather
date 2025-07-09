package com.example.weatherapp.repository;

import com.example.weatherapp.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    
    /**
     * Trova le previsioni meteo per una città specifica a partire dalla data odierna
     * @param city nome della città
     * @param startDate data di inizio (inclusa)
     * @return lista di previsioni meteo
     */
    List<WeatherEntity> findByCityAndDateGreaterThanEqualOrderByDateAsc(String city, LocalDate startDate);
    
    /**
     * Trova tutte le città distinte presenti nel database
     * @return lista di nomi di città
     */
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT w.city FROM WeatherEntity w")
    List<String> findDistinctCity();
    
    /**
     * Elimina tutte le previsioni meteo per una città specifica
     * @param city nome della città
     */
    void deleteByCityIgnoreCase(String city);
}