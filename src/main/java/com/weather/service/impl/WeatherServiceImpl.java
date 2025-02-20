package com.weather.service.impl;

import com.weather.service.WeatherService;
import com.weather.util.TemperatureConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.weather.dto.ForecastResponse;
import com.weather.dto.TemperatureResponse;
import com.weather.dto.WeatherApiResponse;
import com.weather.model.Weather;
import com.weather.repository.WeatherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final WeatherRepository weatherRepository;
    private final TemperatureConverter temperatureConverter;

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherRepository weatherRepository, TemperatureConverter temperatureConverter) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
        this.temperatureConverter = temperatureConverter;
    }

    @Override
    public TemperatureResponse getTemperature(WeatherApiResponse weatherData) {

        // TODO Completar el metodo
        // analizar el objeto recibido para realizar la conversion de temperaturas
        // persistir en DB
        // generar la respuesta

    }

    @Override
    public ForecastResponse getForecast(WeatherApiResponse weatherData) {

    }

    @Override
    public List<Weather> getAllWeather(String country, String unit) {
        if (!unit.matches("(?i)[CFK]")) {
            throw new IllegalArgumentException("Invalid temperature unit. Use C, F, or K");
        }


    }

    private String generateForecastMessage(String weather, double tempCelsius) {

    }
}
