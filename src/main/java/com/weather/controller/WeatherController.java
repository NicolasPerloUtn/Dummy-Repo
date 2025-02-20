package com.weather.controller;

import com.weather.dto.WeatherAlertResponse;
import com.weather.dto.WeatherApiResponse;
import com.weather.service.WeatherRiskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.weather.dto.ForecastResponse;
import com.weather.dto.TemperatureResponse;
import com.weather.model.Weather;
import com.weather.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/clima")
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherRiskService weatherRiskService;

    @PostMapping("/temperatura")
    public ResponseEntity<TemperatureResponse> getTemperature() {
        // TODO  Completar los parametros del endpoint
        //       Completar el llamado al servicio
    }

    @PostMapping("/pronostico")
    public ResponseEntity<ForecastResponse> getForecast() {
        // TODO  Completar los parametros del endpoint
        //       Completar el llamado al servicio
    }

    @GetMapping("/")
    public ResponseEntity<List<Weather>> getAllWeather() {

    }

    @GetMapping("/alertas/{country}")
    public ResponseEntity<WeatherAlertResponse> getWeatherAlerts() {

    }

}
