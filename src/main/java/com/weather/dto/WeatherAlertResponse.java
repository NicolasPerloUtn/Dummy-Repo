package com.weather.dto;

import lombok.Data;
import java.util.List;

@Data
public class WeatherAlertResponse {
    private String country;
    private CityRiskInfo highestRiskCity;
    private List<CityRiskInfo> allCitiesRisk;
    private String alertMessage;

    @Data
    public static class CityRiskInfo {
        private String cityName;
        private double riskScore;
        private List<RiskFactor> riskFactors;
    }

    @Data
    public static class RiskFactor {
        private String factor;
        private String description;
        private String severity;
    }
}