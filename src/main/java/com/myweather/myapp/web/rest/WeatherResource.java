package com.myweather.myapp.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myweather.myapp.domain.Weather;
import com.myweather.myapp.repository.WeatherRepository;
import com.myweather.myapp.security.AuthoritiesConstants;
import com.myweather.myapp.service.WeatherService;
import com.myweather.myapp.service.dto.WeatherCitySearchDto;
import com.myweather.myapp.web.rest.errors.BadRequestAlertException;

import com.myweather.myapp.web.rest.vm.CitiesAverageTemperatureVM;
import com.myweather.myapp.web.rest.vm.CitiesWeatherVM;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.undertow.util.BadRequestException;
import java.text.ParseException;
import javax.inject.Inject;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.myweather.myapp.domain.Weather}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WeatherResource {

    private final Logger log = LoggerFactory.getLogger(WeatherResource.class);

    private static final String ENTITY_NAME = "weather";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Inject
    WeatherService weatherService;

    private final WeatherRepository weatherRepository;

    public WeatherResource(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * {@code POST  /citiesWeatherData} : Create a new weather.
     *
     * @param cityNames for the weather data to create.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} with the body of cities, or with status {@code 400 (Bad Request)} if the city names are
     * of incorrect data type.
     * @throws BadRequestException exception throw in case if incorrect data type is sent or if more than three cities have been sent
     */
    @PostMapping("/citiesWeatherData")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<CitiesWeatherVM>> readCityWeatherData(@Valid @RequestBody List<String> cityNames)
        throws BadRequestException {
        log.debug("REST request to read city weather data : {}", cityNames);

        final List<CitiesWeatherVM> cities = weatherService.saveWeatherForCities(cityNames);

        return ResponseEntity.ok().body(cities);
    }

    /**
     * {@code POST  /getAverageWeatherData} : Create a new weather.
     *
     * @param weatherCitySearchDto for searching the weather data.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with the body of cities with average temperatures for five days, or with status
     * {@code 400 (Bad Request)} if the city ids are of incorrect data type.
     * @throws BadRequestException exception throw in case if incorrect data type is sent or if more than three cities have been sent
     */
    @PostMapping("/getAverageWeatherData")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<CitiesAverageTemperatureVM>> getAverageWeatherDataSorted(@Valid @RequestBody WeatherCitySearchDto weatherCitySearchDto)
        throws BadRequestException {
        log.debug("REST request to read city weather data : {}", weatherCitySearchDto);

        final List<CitiesAverageTemperatureVM> cities = weatherService.readWeatherDataSorted(weatherCitySearchDto);

        return ResponseEntity.ok().body(cities);
    }

    /**
     * {@code POST  /weathers} : Create a new weather.
     *
     * @param weather the weather to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weather, or with status {@code 400 (Bad Request)} if the
     * weather has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weathers")
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) throws URISyntaxException {
        log.debug("REST request to save Weather : {}", weather);
        if (weather.getId() != null) {
            throw new BadRequestAlertException("A new weather cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Weather result = weatherRepository.save(weather);
        return ResponseEntity.created(new URI("/api/weathers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weathers} : Updates an existing weather.
     *
     * @param weather the weather to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weather, or with status {@code 400 (Bad Request)} if the
     * weather is not valid, or with status {@code 500 (Internal Server Error)} if the weather couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weathers")
    public ResponseEntity<Weather> updateWeather(@RequestBody Weather weather) throws URISyntaxException {
        log.debug("REST request to update Weather : {}", weather);
        if (weather.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Weather result = weatherRepository.save(weather);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weather.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /weathers} : get all the weathers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weathers in body.
     */
    @GetMapping("/weathers")
    public List<Weather> getAllWeathers() {
        log.debug("REST request to get all Weathers");
        return weatherRepository.findAll();
    }

    /**
     * {@code GET  /weathers/:id} : get the "id" weather.
     *
     * @param id the id of the weather to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weather, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weathers/{id}")
    public ResponseEntity<Weather> getWeather(@PathVariable Long id) {
        log.debug("REST request to get Weather : {}", id);
        Optional<Weather> weather = weatherRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weather);
    }

    /**
     * {@code DELETE  /weathers/:id} : delete the "id" weather.
     *
     * @param id the id of the weather to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weathers/{id}")
    public ResponseEntity<Void> deleteWeather(@PathVariable Long id) {
        log.debug("REST request to delete Weather : {}", id);
        weatherRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
