# Weather App

## Descrizione
Questa è un'applicazione web per le previsioni meteo che utilizza l'API di Open-Meteo per ottenere dati meteo reali, con fallback su un database H2 integrato. L'applicazione è costruita con Spring Boot, utilizza Spring Data JPA per l'accesso al database e Thymeleaf per il rendering delle pagine HTML.

## Funzionalità
- Visualizzazione delle previsioni meteo reali per i prossimi 7 giorni tramite Open-Meteo API
- Selezione delle previsioni meteo da un menu a tendina di città italiane
- Aggiunta di nuove città tramite campo di testo
- Visualizzazione di icone meteo basate sulle condizioni atmosferiche
- Visualizzazione di grafici per temperatura, umidità e velocità del vento
- Fallback automatico su database H2 in caso di problemi con l'API
- Persistenza dei dati in un database H2 in-memory

## Requisiti
- Java 17 o superiore
- Maven
- Docker (per l'esecuzione in container)
- Docker Compose (opzionale, per l'orchestrazione dei container)

## Architettura dell'applicazione

L'applicazione è strutturata secondo il pattern MVC (Model-View-Controller) con i seguenti componenti principali:

### Model
- **WeatherEntity**: Entità JPA che rappresenta i dati meteo nel database H2
- **WeatherData**: Classe DTO (Data Transfer Object) utilizzata per trasferire i dati meteo al frontend

### Repository
- **WeatherRepository**: Interfaccia JPA Repository per l'accesso ai dati meteo nel database

### Service
- **WeatherService**: Servizio che gestisce la logica di business, coordina il recupero dei dati da Open-Meteo API e gestisce il fallback sul database
- **OpenMeteoService**: Servizio che gestisce le chiamate all'API di Open-Meteo con retry automatico
- **GeocodingService**: Servizio che gestisce le coordinate geografiche delle città italiane
- **WeatherDataInitializer**: Componente che inizializza il database con dati meteo predefiniti all'avvio dell'applicazione

### Controller
- **WeatherController**: Controller che gestisce le richieste HTTP, interagisce con il servizio e prepara i dati per la vista

### View
- **weather.html**: Template Thymeleaf che mostra le previsioni meteo e permette la selezione/inserimento delle città

## Configurazione

### Database H2
L'applicazione utilizza un database H2 in-memory per memorizzare e generare i dati meteo. La configurazione è già presente nel file `src/main/resources/application.properties`:

```properties
# Database H2 Configuration
spring.datasource.url=jdbc:h2:mem:weatherdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Weather Data Configuration
weather.data.descriptions=Soleggiato,Nuvoloso,Parzialmente nuvoloso,Piovoso,Temporale,Nebbioso,Nevoso

# Open Meteo API Configuration
weather.api.retry.max-attempts=3
weather.api.retry.delay=1000
weather.api.timeout=5000
```

Non è necessaria alcuna configurazione aggiuntiva per iniziare a utilizzare l'applicazione.

## Esecuzione dell'applicazione con Docker

### Preparazione

L'applicazione è pronta per essere avviata senza configurazioni aggiuntive, poiché utilizza un database H2 in-memory per generare i dati meteo.

### Opzione 1: Build e avvio manuale con Docker

1. **Build dell'immagine Docker**

   ```bash
   docker build -t weather-app .
   ```

   Questo comando crea un'immagine Docker chiamata `weather-app` utilizzando il Dockerfile presente nella directory corrente.

2. **Avvio del container**

   ```bash
   docker run -p 8080:8080 --name weather-container weather-app
   ```

   Questo comando avvia un container chiamato `weather-container` basato sull'immagine `weather-app` e mappa la porta 8080 del container alla porta 8080 dell'host.

3. **Avvio del container in background**

   ```bash
   docker run -d -p 8080:8080 --name weather-container weather-app
   ```

   L'opzione `-d` avvia il container in modalità detached (in background).

### Opzione 2: Utilizzo di Docker Compose

1. **Avvio con Docker Compose**

   ```bash
   docker-compose up
   ```

   Questo comando avvia l'applicazione utilizzando la configurazione definita nel file `docker-compose.yml`.

2. **Avvio in background con Docker Compose**

   ```bash
   docker-compose up -d
   ```

   L'opzione `-d` avvia i container in modalità detached (in background).

3. **Arresto dei container**

   ```bash
   docker-compose down
   ```

   Questo comando arresta e rimuove i container, le reti e i volumi creati da `docker-compose up`.

### Gestione dei container Docker

- **Visualizzazione dei container in esecuzione**

  ```bash
  docker ps
  ```

- **Visualizzazione di tutti i container (anche quelli arrestati)**

  ```bash
  docker ps -a
  ```

- **Arresto di un container**

  ```bash
  docker stop weather-container
  ```

- **Riavvio di un container**

  ```bash
  docker restart weather-container
  ```

- **Rimozione di un container**

  ```bash
  docker rm weather-container
  ```

- **Visualizzazione dei log del container**

  ```bash
  docker logs weather-container
  ```

  Per seguire i log in tempo reale:

  ```bash
  docker logs -f weather-container
  ```

## Accesso all'applicazione
Una volta avviata, l'applicazione sarà accessibile all'indirizzo: http://localhost:8080

## Utilizzo
- La pagina principale mostra le previsioni meteo per Roma (predefinito)
- Utilizza il menu a tendina per selezionare una città tra quelle disponibili
- In alternativa, inserisci il nome di una nuova città nel campo di testo e clicca su "Cerca"
- Le previsioni mostrano data, condizioni meteo, temperatura, umidità e velocità del vento
- Quando inserisci una nuova città, l'applicazione genera automaticamente dati meteo casuali ma realistici per i prossimi 7 giorni
- La nuova città viene aggiunta al database e sarà disponibile nel menu a tendina per le future ricerche

## Risoluzione dei problemi

### Il container si avvia ma l'applicazione non è accessibile

1. Verifica che il container sia in esecuzione:
   ```bash
   docker ps
   ```

2. Controlla i log del container per eventuali errori:
   ```bash
   docker logs weather-container
   ```

3. Verifica che la porta 8080 sia correttamente mappata e non sia già in uso da altre applicazioni.

### Accesso alla console H2

Per accedere alla console del database H2 e visualizzare/modificare i dati direttamente:

1. Apri un browser e vai all'indirizzo: http://localhost:8080/h2-console
2. Inserisci i seguenti dati di connessione:
   - JDBC URL: `jdbc:h2:mem:weatherdb`
   - Username: `sa`
   - Password: `password`
3. Clicca su "Connect" per accedere al database

### Problemi con il recupero dei dati meteo

1. Se l'applicazione non riesce a recuperare i dati meteo da Open-Meteo API:
   - Controlla i log dell'applicazione per eventuali errori di connessione
   - Verifica che la città richiesta sia tra quelle italiane supportate
   - L'applicazione tenterà automaticamente di recuperare i dati dal database H2
2. Se il fallback sul database non funziona:
   - Verifica che il database H2 sia correttamente configurato nel file `application.properties`
   - Controlla che la proprietà `weather.data.descriptions` sia correttamente impostata

### Ricostruzione dell'immagine dopo modifiche al codice

Se hai apportato modifiche al codice, dovrai ricostruire l'immagine Docker:

```bash
docker build -t weather-app .
docker stop weather-container
docker rm weather-container
docker run -p 8080:8080 --name weather-container weather-app
```

Oppure con Docker Compose:

```bash
docker-compose down
docker-compose build
docker-compose up
```