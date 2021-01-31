package com.myweather.myapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweather.myapp.domain.City;
import com.myweather.myapp.domain.Weather;
import com.myweather.myapp.models.WeatherData;
import com.myweather.myapp.models.WeatherTemperature;
import com.myweather.myapp.models.WeatherTemperatureList;
import com.myweather.myapp.repository.CityRepository;
import com.myweather.myapp.repository.WeatherRepository;
import com.myweather.myapp.service.dto.CitySearchDto;
import com.myweather.myapp.models.WeatherApiData;
import com.myweather.myapp.web.rest.vm.CitiesWeatherVM;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import liquibase.pro.packaged.W;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class WeatherService {

    @Inject
    RestTemplate restTemplate;

    @Inject
    WeatherApiData weatherAppData;

    @Inject
    CityRepository cityRepository;

    @Inject
    WeatherRepository weatherRepository;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<CitiesWeatherVM> saveWeatherForCities(CitySearchDto city) throws ParseException, JsonProcessingException {

        final List<City> cities = cityRepository.findCitiesByName(city.getCities());

        // TODO make this message better
        if (cities.isEmpty()) {
            throw new NoResultException("No cities found for names " + city.getCities());
        }

        final List<CitiesWeatherVM> savedCitiesData = new ArrayList<>();

        for (City foundCity : cities) {
            final UriComponents uriComponents = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host(weatherAppData.getWeatherApiUrl())
                .path("")
                .query("q={city}&appid={appid}")
                .buildAndExpand(foundCity.getId(), weatherAppData.getWeatherApiKey());

            final ResponseEntity<WeatherTemperatureList> response = restTemplate
                .getForEntity(uriComponents.toUriString(), WeatherTemperatureList.class);

            log.debug("Saving weather data for city {}", foundCity.getName());
            if (Optional.ofNullable(response.getBody()).isPresent() && Optional.ofNullable(response.getBody().getList()).isPresent()) {
                for (WeatherTemperature weatherTemperature : response.getBody().getList()) {
                    if (Optional.ofNullable(weatherTemperature.getMain()).isPresent()) {
                        final WeatherData mainData = weatherTemperature.getMain();

                        final Weather weather = new Weather(mainData.getTemp(), mainData.getFeelsLike(), mainData.getTempMin(), mainData.getTempMax(),
                            mainData.getDate(), foundCity);

                        weatherRepository.save(weather);
                    } else {
                        throw new NoResultException("Response body main for city " + foundCity.getName() + " is empty!");
                    }
                }
                savedCitiesData.add(new CitiesWeatherVM(foundCity.getId(), foundCity.getName(), foundCity.getOpenWeatherId()));

            } else {
                throw new NoResultException("Response body for city " + foundCity.getName() + " is empty!");
            }
        }

        return savedCitiesData;
    }

}
