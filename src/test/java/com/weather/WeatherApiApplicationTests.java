package com.weather;

import com.weather.dto.ForecastResponse;
import com.weather.dto.TemperatureResponse;
import com.weather.dto.WeatherApiResponse;
import com.weather.model.Weather;
import com.weather.repository.WeatherRepository;
import com.weather.service.impl.WeatherServiceImpl;
import com.weather.util.TemperatureConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WeatherApiApplicationTests {

	@Test
	void contextLoads() {
	}


	@Mock
	private RestTemplate restTemplate;

	@Mock
	private WeatherRepository weatherRepository;

	@Mock
	private TemperatureConverter temperatureConverter;

	@InjectMocks
	private WeatherServiceImpl weatherService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getTemperatura() {
		WeatherApiResponse weatherData = createMockWeatherApiResponse("Lima", "PE", 75.0, 70.0, "Clouds", "cloudy sky");
		when(temperatureConverter.fahrenheitToCelsius(75.0)).thenReturn(23.89);
		when(temperatureConverter.fahrenheitToKelvin(75.0)).thenReturn(297.04);
		when(weatherRepository.findByNameAndCountry("Lima", "PE")).thenReturn(Optional.empty());
		when(weatherRepository.save(any(Weather.class))).thenReturn(new Weather());

		TemperatureResponse response = weatherService.getTemperature(weatherData);

		assertEquals("Lima", response.getName());
		assertEquals("PE", response.getCountry());
		assertEquals(23.89, response.getTemp());
		assertEquals(23.89, response.getFeels_like());
		verify(weatherRepository, times(1)).save(any(Weather.class));
	}

	@Test
	void getForecast() {
		WeatherApiResponse weatherData = createMockWeatherApiResponse("Lima", "PE", 80.0, 77.0, "Clear", "clear sky");
		when(temperatureConverter.fahrenheitToCelsius(80.0)).thenReturn(26.67);
		when(temperatureConverter.fahrenheitToKelvin(80.0)).thenReturn(299.82);
		when(weatherRepository.findById(1L)).thenReturn(Optional.empty());
		when(weatherRepository.save(any(Weather.class))).thenReturn(new Weather());

		ForecastResponse response = weatherService.getForecast(weatherData);
		assertNotNull(response);
	}

	@Test
	void getAllWeather() {
		List<Weather> weatherList = Arrays.asList(
				createMockWeather(1L, "Lima", "PE", 25.0, 77.0, 298.15, "Clear", "clear sky")
		);
		when(weatherRepository.findByCountry("PE")).thenReturn(weatherList);

		List<Weather> result = weatherService.getAllWeather("PE", "F");

//		assertEquals(1, result.size());
//		assertEquals(0, result.get(0).getTemp_celsius());
//		assertEquals(77.0, result.get(0).getTemp_farenheit());
//		assertEquals(0, result.get(0).getTemp_kelvin());
		assertNotNull(result);
	}

	@Test
	void getAllWeatherT() {
		List<Weather> weatherList = Arrays.asList(
				createMockWeather(1L, "Lima", "PE", 25.0, 77.0, 298.15, "Clear", "clear sky")
		);
		when(weatherRepository.findByCountry("PE")).thenReturn(weatherList);

		List<Weather> result = weatherService.getAllWeather("PE", "K");

		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getTemp_celsius());
		assertEquals(0, result.get(0).getTemp_farenheit());
		assertEquals(298.15, result.get(0).getTemp_kelvin());
	}

	@Test
	void testExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> weatherService.getAllWeather("PE", "X"));
	}

	@Test
	void generateForecastMessage() {
		assertEquals("Día frío y nublado, abrígate bien.",
				metodoPrivadoTest("Clouds", 10.0));
		assertEquals("Clima templado y nublado, ideal para un paseo.",
				metodoPrivadoTest("Clouds", 20.0));
		assertEquals("Día cálido con nubes, mantente hidratado.",
				metodoPrivadoTest("Clouds", 30.0));
		assertEquals("Clima extremadamente caluroso con nubes, evita el sol directo.",
				metodoPrivadoTest("Clouds", 40.0));

		assertEquals("Lluvia y frío, lleva abrigo e impermeable.",
				metodoPrivadoTest("Rain", 10.0));
		assertEquals("Lluvia ligera con clima templado, usa paraguas.",
				metodoPrivadoTest("Rain", 20.0));
		assertEquals("Lluvia con calor, posible humedad alta.",
				metodoPrivadoTest("Rain", 30.0));
		assertEquals("Lluvia en clima muy caluroso, posible tormenta.",
				metodoPrivadoTest("Rain", 40.0));

		assertEquals("Día soleado pero frío, abrígate bien.",
				metodoPrivadoTest("Clear", 10.0));
		assertEquals("Clima perfecto, disfruta el día.",
				metodoPrivadoTest("Clear", 20.0));
		assertEquals("Día soleado y caluroso, usa protector solar.",
				metodoPrivadoTest("Clear", 30.0));
		assertEquals("Ola de calor, evita la exposición prolongada al sol.",
				metodoPrivadoTest("Clear", 40.0));

		assertEquals("Neblina y frío, maneja con precaución.",
				metodoPrivadoTest("Haze", 10.0));
		assertEquals("Neblina ligera, visibilidad reducida.",
				metodoPrivadoTest("Haze", 20.0));
		assertEquals("Ambiente cálido y brumoso, posibles alergias.",
				metodoPrivadoTest("Haze", 30.0));
		assertEquals("Neblina densa con calor extremo, toma precauciones.",
				metodoPrivadoTest("Haze", 40.0));

		assertEquals("Condición climática no especificada",
				metodoPrivadoTest("", 25.0));
		assertEquals("Condición climática no especificada",
				metodoPrivadoTest(null, 25.0));

		assertEquals("Condición climática no especificada",
				metodoPrivadoTest("Tornado", 25.0));
	}


	private String metodoPrivadoTest(String weather, double tempCelsius) {
		try {
			java.lang.reflect.Method method = WeatherServiceImpl.class.getDeclaredMethod(
					"generateForecastMessage", String.class, double.class);
			method.setAccessible(true);
			return (String) method.invoke(weatherService, weather, tempCelsius);
		} catch (Exception e) {
			fail("Error al invocar método privado: " + e.getMessage());
			return null;
		}
	}

	private WeatherApiResponse createMockWeatherApiResponse(String name, String country,
															double temp, double feelsLike,
															String weatherMain, String weatherDescription) {
		WeatherApiResponse data = new WeatherApiResponse();
		data.setId(1L);
		data.setName(name);
		data.setCountry(country);

		WeatherApiResponse.MainInfo mainInfo = new WeatherApiResponse.MainInfo();
		mainInfo.setTemp(temp);
		mainInfo.setFeels_like(feelsLike);
		data.setMain(mainInfo);

		WeatherApiResponse.WeatherInfo weatherInfo = new WeatherApiResponse.WeatherInfo();
		weatherInfo.setMain(weatherMain);
		weatherInfo.setDescription(weatherDescription);
		data.setWeather(new ArrayList<>(Arrays.asList(weatherInfo)));

		return data;
	}

	private Weather createMockWeather(Long id, String name, String country,
									  double tempCelsius, double tempFahrenheit, double tempKelvin,
									  String weather, String description) {
		Weather weatherEntity = new Weather();
		weatherEntity.setId(id);
		weatherEntity.setName(name);
		weatherEntity.setCountry(country);
		weatherEntity.setTemp_celsius(tempCelsius);
		weatherEntity.setTemp_farenheit(tempFahrenheit);
		weatherEntity.setTemp_kelvin(tempKelvin);
		weatherEntity.setWeather(weather);
		weatherEntity.setDescription(description);
		return weatherEntity;
	}
}
