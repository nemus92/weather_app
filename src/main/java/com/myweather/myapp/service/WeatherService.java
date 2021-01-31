package com.myweather.myapp.service;

import com.myweather.myapp.domain.City;
import com.myweather.myapp.domain.Weather;
import com.myweather.myapp.models.WeatherData;
import com.myweather.myapp.models.WeatherTemperature;
import com.myweather.myapp.models.WeatherTemperatures;
import com.myweather.myapp.repository.CityRepository;
import com.myweather.myapp.repository.WeatherRepository;
import com.myweather.myapp.models.WeatherApiData;
import com.myweather.myapp.service.dto.WeatherCitySearchDto;
import com.myweather.myapp.web.rest.vm.CitiesAverageTemperatureVM;
import com.myweather.myapp.web.rest.vm.CitiesWeatherVM;
import io.undertow.util.BadRequestException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<CitiesWeatherVM> saveWeatherForCities(List<String> cityNames) throws BadRequestException {

        if (cityNames.size() > 3) {
            throw new BadRequestException("Attempting to save weather for more than three cities! Maximum size is 3!");
        }

        final List<City> cities = cityRepository.findCitiesByName(cityNames);

        if (cities.isEmpty()) {
            throw new NoResultException("No cities found for names " + cityNames);
        }

        if (cities.size() != cityNames.size()) {
            log.debug("Not all cities have been found, only {}", cities.stream().map(City::getName).collect(Collectors.toList()));
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

            final ResponseEntity<WeatherTemperatures> response = restTemplate
                .getForEntity(uriComponents.toUriString(), WeatherTemperatures.class);

            log.debug("Saving weather data for city {}", foundCity.getName());
            if (Optional.ofNullable(response.getBody()).isPresent() && Optional.ofNullable(response.getBody().getList()).isPresent()) {
                for (WeatherTemperature weatherTemperature : response.getBody().getList()) {
                    if (Optional.ofNullable(weatherTemperature.getMain()).isPresent()) {
                        final WeatherData mainData = weatherTemperature.getMain();

                        final Weather weather = new Weather(mainData.getTemp(), mainData.getFeelsLike(), mainData.getTempMin(), mainData.getTempMax(),
                            weatherTemperature.getDate(), foundCity);

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

    public List<CitiesAverageTemperatureVM> readWeatherDataSorted(WeatherCitySearchDto weatherCitySearchDto) throws BadRequestException {

        if (weatherCitySearchDto.getCityIds().size() > 3) {
            throw new BadRequestException("Attempting to read weather for more than three cities! Maximum size is 3!");
        }

        // TODO read all weather data for params, so to have less queries later
        final List<Weather> weathers = weatherRepository
            .getWeatherDataForDates(weatherCitySearchDto.getDateFrom(), weatherCitySearchDto.getDateTo(), weatherCitySearchDto.getCityIds());

        // TODO used Set to remove duplicates
        final Set<City> cities = weathers.stream().distinct().map(Weather::getCity).collect(Collectors.toSet());

        if (weathers.isEmpty()) {
            throw new NoResultException(
                "No weather data for city ids " + weatherCitySearchDto.getCityIds() + " between dates " + weatherCitySearchDto.getDateFrom() + ", and " + weatherCitySearchDto.getDateTo());
        }

        final List<CitiesAverageTemperatureVM> citiesAverageTemperatureVMS = new ArrayList<>();

        for (City city : cities) {
            double averageTemperature = 0.0;
            double sumTemperature = 0.0;

            final List<Double> temperaturesForCity = weathers.stream().filter(weather -> weather.getCity().getId().equals(city.getId()))
                .map(Weather::getTemperature).collect(Collectors.toList());

            for (Double temperature : temperaturesForCity) {
                sumTemperature += temperature;

                averageTemperature = sumTemperature / temperaturesForCity.size();
            }

            final CitiesAverageTemperatureVM citiesWeatherVM = new CitiesAverageTemperatureVM(city.getId(), city.getName(), averageTemperature);
            citiesAverageTemperatureVMS.add(citiesWeatherVM);
        }

        // TODO sorting of cities with avg temperature by temperature
        citiesAverageTemperatureVMS.sort(Comparator.comparing(CitiesAverageTemperatureVM::getAverageTemperature));

        return citiesAverageTemperatureVMS;
    }
}
