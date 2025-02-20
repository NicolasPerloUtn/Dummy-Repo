package com.weather.service.impl;

import com.weather.dto.WeatherAlertResponse;
import com.weather.dto.WeatherAlertResponse.CityRiskInfo;
import com.weather.dto.WeatherAlertResponse.RiskFactor;
import com.weather.model.Weather;
import com.weather.repository.WeatherRepository;
import com.weather.service.WeatherRiskService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class WeatherRiskServiceImpl implements WeatherRiskService {

    private final WeatherRepository weatherRepository;

    public WeatherRiskServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public WeatherAlertResponse analyzeCountryRisks(String country) {
        List<Weather> cities = weatherRepository.findByCountry(country);
        if (cities.isEmpty()) {
            throw new RuntimeException("No weather data found for country: " + country);
        }

        List<CityRiskInfo> cityRisks = new ArrayList<>();

        for (Weather city : cities) {
            CityRiskInfo riskInfo = analyzeCityRisk(city);
            cityRisks.add(riskInfo);
        }

        CityRiskInfo highestRiskCity = cityRisks.stream()
                .max(Comparator.comparing(CityRiskInfo::getRiskScore))
                .orElseThrow();

        WeatherAlertResponse response = new WeatherAlertResponse();
        response.setCountry(country);
        response.setHighestRiskCity(highestRiskCity);
        response.setAllCitiesRisk(cityRisks);
        response.setAlertMessage(generateAlertMessage(highestRiskCity));

        return response;
    }

    private CityRiskInfo analyzeCityRisk(Weather city) {
        // TODO   Completar el metodo
        //        completar el analisis de riesgo, Riesgos por temperatura y Riesgos por Clima
        //        tener en cuenta la tabla de valores para calcular el RiskScore
        //        retornar el CityRiskInfo calculado

//        return cityRisk;

        return null;
    }

    private String generateAlertMessage(CityRiskInfo highestRiskCity) {
        if (highestRiskCity.getRiskScore() == 0) {
            return "No se detectaron riesgos significativos en ninguna ciudad.";
        }

        StringBuilder message = new StringBuilder();
        message.append("¡Alerta! ").append(highestRiskCity.getCityName())
                .append(" presenta el mayor riesgo climático.\n");

        for (RiskFactor risk : highestRiskCity.getRiskFactors()) {
            message.append("- ").append(risk.getDescription())
                    .append(" (Severidad: ").append(risk.getSeverity()).append(")\n");
        }

        return message.toString();
    }
}
