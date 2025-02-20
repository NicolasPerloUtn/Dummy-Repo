package com.weather.service;

import com.weather.dto.WeatherAlertResponse;

public interface WeatherRiskService {

    public WeatherAlertResponse analyzeCountryRisks(String country);

}
