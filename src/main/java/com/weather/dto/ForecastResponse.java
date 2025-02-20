package com.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastResponse {
    private Long id;
    private String name;
    private String country;
    private WeatherInfo weather;

    @Data
    public static class WeatherInfo {
        private String main;
        private String description;
    }
}