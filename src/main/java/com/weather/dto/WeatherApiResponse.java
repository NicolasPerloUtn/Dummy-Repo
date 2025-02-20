package com.weather.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiResponse {
    private Long id;
    private String name;
    private String country;
    private Coordinates coord;
    private List<WeatherInfo> weather;
    private MainInfo main;
    private Integer visibility;
    private Wind wind;
    private Clouds clouds;

    @Data
    public static class Coordinates {
        private Double lon;
        private Double lat;
    }

    @Data
    public static class WeatherInfo {
        private String main;
        private String description;
    }

    @Data
    public static class MainInfo {
        private Double temp;
        private Double feels_like;
        private Double temp_min;
        private Double temp_max;
        private Integer pressure;
        private Integer humidity;
        private Integer sea_level;
        private Integer grnd_level;
    }

    @Data
    public static class Wind {
        private Double speed;
        private Integer deg;
    }

    @Data
    public static class Clouds {
        private Integer all;
    }
}
