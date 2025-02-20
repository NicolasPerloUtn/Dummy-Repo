package com.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.weather.model.Weather;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>, JpaSpecificationExecutor<Weather> {
    List<Weather> findByCountry(String country);

    Optional<Weather> findByNameAndCountry(String cityName, String country);
}
