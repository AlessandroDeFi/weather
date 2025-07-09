# Applicazione Previsioni Meteo

Questa è un'applicazione Spring Boot che mostra le previsioni meteo per la settimana corrente con un'interfaccia utente minimal e responsive.

## Caratteristiche

- Visualizzazione delle previsioni meteo per i prossimi 7 giorni
- Interfaccia utente responsive e minimal
- Dati meteo simulati che includono:
  - Temperatura
  - Umidità
  - Velocità del vento
  - Descrizione delle condizioni meteorologiche
- Localizzazione in italiano

## Requisiti

- Java 17 o superiore
- Maven 3.8+ (per build locale)
- Docker e Docker Compose (per esecuzione containerizzata)

## Esecuzione Locale

1. Clonare il repository
2. Navigare nella directory del progetto
3. Eseguire l'applicazione con Maven:
   ```bash
   mvn spring-boot:run
   ```
4. Aprire il browser e visitare: http://localhost:8080

## Esecuzione con Docker

1. Assicurarsi che Docker e Docker Compose siano installati e in esecuzione
2. Navigare nella directory del progetto
3. Costruire e avviare il container:
   ```bash
   docker-compose up --build
   ```
4. Aprire il browser e visitare: http://localhost:8080

## Struttura del Progetto

```
├── src/main/
│   ├── java/com/example/weatherapp/
│   │   ├── WeatherApplication.java
│   │   ├── controller/
│   │   │   └── WeatherController.java
│   │   ├── model/
│   │   │   └── WeatherData.java
│   │   └── service/
│   │       └── WeatherService.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           └── weather.html
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## Tecnologie Utilizzate

- Spring Boot 3.1.5
- Thymeleaf per il template engine
- Maven per la gestione delle dipendenze
- Docker per la containerizzazione
- Bootstrap per lo styling (integrato nel template)

## Sviluppo

Per contribuire al progetto:

1. Creare un fork del repository
2. Creare un branch per le modifiche
3. Effettuare le modifiche
4. Inviare una pull request

## Note

- L'applicazione utilizza dati meteo simulati per scopo dimostrativo
- Il server si avvia sulla porta 8080 di default
- L'applicazione è configurata per l'esecuzione in italiano

## Licenza

Questo progetto è distribuito con licenza MIT. Vedere il file LICENSE per maggiori dettagli.