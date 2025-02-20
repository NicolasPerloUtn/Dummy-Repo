package com.weather.util;

import org.springframework.stereotype.Component;

@Component
public class TemperatureConverter {

    public double fahrenheitToCelsius(double fahrenheit) {
        // TODO  Completar formula
    }

    public double fahrenheitToKelvin(double fahrenheit) {
        // TODO  Completar formula
    }

    public double celsiusToFahrenheit(double celsius) {
        return celsius * 9/5 + 32;
    }

    public double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public double kelvinToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 9/5 + 32;
    }

    public double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public double getTemperatureInUnit(double tempCelsius, double tempFahrenheit, double tempKelvin, String unit) {
        return switch (unit.toUpperCase()) {
            case "C" -> tempCelsius;
            case "F" -> tempFahrenheit;
            case "K" -> tempKelvin;
            default -> throw new IllegalArgumentException("Invalid temperature unit. Use C, F, or K");
        };
    }
}
