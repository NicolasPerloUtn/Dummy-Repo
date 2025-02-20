[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Ygq4CJL-)
# Weather API Service

## Overview
Una sofisticada aplicación Spring Boot que proporciona información y análisis del clima a través de varios endpoints. El servicio obtiene datos meteorológicos de una API externa y los procesa para proporcionar conversiones de temperatura, pronósticos meteorológicos y análisis de riesgos.

## Features
- Datos de temperatura en múltiples unidades (Celsius, Fahrenheit, Kelvin)
- Pronóstico del tiempo con mensajes contextuales basados ​​en las condiciones
- Almacenamiento de datos meteorológicos históricos
- Análisis de riesgo meteorológico por país
- Alertas climáticas integrales por ciudad
- Implementación en contenedores con Docker (solo LCIV)

## Database Schema General
```sql
CREATE TABLE CLIMA (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    country VARCHAR(2),
    temp_celsius DOUBLE PRECISION,
    temp_fahrenheit DOUBLE PRECISION,
    temp_kelvin DOUBLE PRECISION,
    weather VARCHAR(255),
    description TEXT
);
```

## Precondiciones LCIII
### DB
* Usar H2 para la persistencia de datos
### JSON Base
* Los endpoint a trabajar deberan permitir el manejo del siguiente Json para la manipulación de la información de los puntos a desarrollar:
```json
{
    "weather": [
        {
            "id": 1,
            "name": "Córdoba",
            "country": "AR",
            "coord": {
                "lon": -64.1811,
                "lat": -31.4135
            },
            "weather": [
                {
                    "main": "Clouds",
                    "description": "broken clouds"
                }
            ],
            "main": {
                "temp": 75.506,
                "feels_like": 76.748,
                "temp_min": 75.236,
                "temp_max": 77.18,
                "pressure": 1009,
                "humidity": 85,
                "sea_level": 1009,
                "grnd_level": 953
            },
            "visibility": 10000,
            "wind": {
                "speed": 2.06,
                "deg": 220
            },
            "clouds": {
                "all": 75
            }
        }
    ]        
}
```

## Precondiciones LCIV
### DB
* Usar MySQL dockerizado para la persistencia de datos
* Los datos de conexion deben ser parametrizables para permitir su administracion desde docker-compose

### API Externa

Desarrollar un cliente en RestTemplate para obtener los datos desde la siguiente API:
```http
https://my-json-server.typicode.com/Gabriel-Arriola/fake-api/weather
```


## API Endpoints

### 1. Get Temperature
```http
POST /clima/temperatura?city={cityName}

http://localhost:8080/clima/temperatura?city=Lima
```
* Devuelve información de temperatura para una ciudad específica, convirtiendo de Fahrenheit a las otras unidades. La respuesta debe mostrar la temperatura en Celsius.
* Tabla de conversiones:
#### Grados Celsius = (Grados Fahrenheit − 32) × 5/9
#### Grados Kelvin = (Grados Fahrenheit - 32) x 5/9 + 273.15

* Persistir en la base de datos los valores obtenidos para todas las temperaturas, si el id ya existe actualizar los valores.

Response example:
```json
{
    "id": 6,
    "name": "Lima",
    "country": "PE",
    "temp": 25.14,
    "feels_like": 27.3
}
```

### 2. Get Weather Forecast
```http
POST /clima/pronostico?city={cityName}

http://localhost:8080/clima/pronostico?city=Lima
```
* Proporciona pronóstico del tiempo con mensajes contextuales basados ​​en las condiciones climaticas.

| Clima	    | Temperatura (°C)	| Mensaje Sugerido                                               |
|-----------|-------------------|----------------------------------------------------------------|
| Clouds	| 0 - 15	        | Día frío y nublado, abrígate bien.                             
| Clouds	| 16 - 25	        | Clima templado y nublado, ideal para un paseo.
| Clouds	| 26 - 36	        | Día cálido con nubes, mantente hidratado.
| Clouds	| > 36	            | Clima extremadamente caluroso con nubes, evita el sol directo.
| Rain	    | 0 - 15	        | Lluvia y frío, lleva abrigo e impermeable.
| Rain	    | 16 - 25	        | Lluvia ligera con clima templado, usa paraguas.
| Rain	    | 26 - 36	        | Lluvia con calor, posible humedad alta.
| Rain	    | > 36	            | Lluvia en clima muy caluroso, posible tormenta.
| Clear	    | 0 - 15	        | Día soleado pero frío, abrígate bien.
| Clear	    | 16 - 25	        | Clima perfecto, disfruta el día.
| Clear	    | 26 - 36	        | Día soleado y caluroso, usa protector solar.
| Clear	    | > 36	            | Ola de calor, evita la exposición prolongada al sol.
| Haze	    | 0 - 15	        | Neblina y frío, maneja con precaución.
| Haze	    | 16 - 25	        | Neblina ligera, visibilidad reducida.
| Haze	    | 26 - 36	        | Ambiente cálido y brumoso, posibles alergias.
| Haze	    | > 36	            | Neblina densa con calor extremo, toma precauciones.

* Si no hay una condicion coincidente informar: "Condición climática no especificada"
* Persistir en la base de datos los valores obtenidos, si el id ya existe actualizar los valores.

Response example:
```json
{
    "id": 6,
    "name": "Lima",
    "country": "PE",
    "weather": {
        "main": "Clouds",
        "description": "Día cálido con nubes, mantente hidratado"
    }
}
```

### 3. Get Weather Data
```http
GET /clima/?unit={C|F|K}&country={countryCode}

http://localhost:8080/clima/?country=AR&unit=K
```
* Recupera datos meteorológicos desde la DB con la unidad de temperatura especificada. El parámetro de país es opcional.

Response example:
```json
[
  {
    "id": 1,
    "name": "Córdoba",
    "country": "AR",
    "tempCelsius": null,
    "tempFahrenheit": null,
    "tempKelvin": 297.32,
    "weather": "Clouds",
    "description": "broken clouds"
  },
  {
    "id": 7,
    "name": "Purmamarca",
    "country": "AR",
    "tempCelsius": null,
    "tempFahrenheit": null,
    "tempKelvin": 311.17999999999995,
    "weather": "Clouds",
    "description": "overcast clouds"
  }
]
```

### 4. Get Weather Alerts
```http
GET /clima/alertas/{countryCode}

http://localhost:8080/clima/alertas/AR
```
Analiza los riesgos climáticos en las ciudades de un país y proporciona alertas detalladas.

| Temperatura (°C) | Condición Climática | Factor       | Severidad  | Descripción                                           |
|------------------|--------------------|-------------|------------|-------------------------------------------------------|
| > 35             | -                  | Temperatura | ALTA       | Temperatura extremadamente alta (>35°C)              |
| 30 - 35          | -                  | Temperatura | MEDIA      | Temperatura elevada (30-35°C)                        |
| -                | Rain               | Lluvia      | MEDIA      | Condiciones lluviosas que pueden causar inconvenientes |
| -                | Thunderstorm       | Tormenta    | ALTA       | Tormentas eléctricas que pueden ser peligrosas       |
| -                | Snow               | Nieve       | MEDIA      | Condiciones nevadas que pueden afectar la movilidad  |
| > 35             | Rain               | Temperatura, Lluvia | ALTA, MEDIA | Temperatura extremadamente alta y lluvia         |
| > 35             | Thunderstorm       | Temperatura, Tormenta | ALTA, ALTA | Temperatura extremadamente alta y tormenta eléctrica |
| > 35             | Snow               | Temperatura, Nieve | ALTA, MEDIA | Temperatura extremadamente alta y nieve         |
| 30 - 35          | Rain               | Temperatura, Lluvia | MEDIA, MEDIA | Temperatura elevada y lluvia                    |
| 30 - 35          | Thunderstorm       | Temperatura, Tormenta | MEDIA, ALTA | Temperatura elevada y tormenta eléctrica       |
| 30 - 35          | Snow               | Temperatura, Nieve | MEDIA, MEDIA | Temperatura elevada y nieve                    |

El riesgo total se calcula sumando los puntajes de cada factor identificado:
* Temperatura ALTA: +3.0
* Temperatura MEDIA: +2.0
* Lluvia MEDIA: +2.0
* Tormenta ALTA: +3.0
* Nieve MEDIA: +2.0

Así, por ejemplo, si la ciudad tiene una temperatura de 36°C y tormenta eléctrica, el total de riesgo sería:
3.0 (Temperatura ALTA) + 3.0 (Tormenta ALTA) = 6.0 puntos de riesgo total.

Response example:
```json
{
  "country": "AR",
  "highestRiskCity": {
    "cityName": "Purmamarca",
    "riskScore": 3,
    "riskFactors": [
      {
        "factor": "Temperatura",
        "description": "Temperatura extremadamente alta (38,0°C)",
        "severity": "ALTA"
      }
    ]
  },
  "allCitiesRisk": [
    {
      "cityName": "Córdoba",
      "riskScore": 0,
      "riskFactors": []
    },
    {
      "cityName": "Purmamarca",
      "riskScore": 3,
      "riskFactors": [
        {
          "factor": "Temperatura",
          "description": "Temperatura extremadamente alta (38,0°C)",
          "severity": "ALTA"
        }
      ]
    }
  ],
  "alertMessage": "¡Alerta! Purmamarca presenta el mayor riesgo climático.\n- Temperatura extremadamente alta (38,0°C) (Severidad: ALTA)\n"
}
```

## Testing

### Realizar test unitario de la capa de servicio con un coverage minimo del 80% (excluyente)


## Exclusivo LCIV

### Docker
* Confeccionar el docker file de la aplicación
* Confeccionar el compose para ejecutar la aplicacion dockerizada junto a la base de datos (mysql)


## Criterios de evaluación

### Tiempo total de examen 3hs.

### LCIII

| CONSIGNA                      | PUNTAJE          |
| ----------------------------- |----------------- |
| Manejo de JSON para input de datos	| 5 |
| endpoint 1 - POST /clima/temperatura?city={}	| 10    |
| endpoint 2 - POST /clima/pronostico?city={}	| 10    |
| endpoint 3 - GET /clima/?unit={}&country={}	| 10    |
| endpoint 4 - GET /clima/alertas/	| 15    |
| test (80% cobertura minimo obligatorio)	| 50    |
| ** TOTAL **	| 100   |



### LCIV

| CONSIGNA                      | PUNTAJE          |
| ----------------------------- |----------------- |
| cliente REST	| 5 |
| endpoint 1 - POST /clima/temperatura?city={}	| 10 |
| endpoint 2 - POST /clima/pronostico?city={}	| 10 |
| endpoint 3 - GET /clima/?unit={}&country={}	| 10 |
| endpoint 4 - GET /clima/alertas/	| 15    |
| dockerizado	| 5 |
| test (80% cobertura minimo obligatorio)	| 45    |
| ** TOTAL **	| 100   |
