package com.weather.service;

import com.weather.dto.ForecastResponse;
import com.weather.dto.TemperatureResponse;
import com.weather.dto.WeatherApiResponse;
import com.weather.model.Weather;

import java.util.List;

public interface WeatherService {

    public TemperatureResponse getTemperature(WeatherApiResponse city);
    public ForecastResponse getForecast(WeatherApiResponse city);
    public List<Weather> getAllWeather(String country, String unit);

}
