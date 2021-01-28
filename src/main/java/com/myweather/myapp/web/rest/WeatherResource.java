package com.myweather.myapp.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myweather.myapp.domain.User;
import com.myweather.myapp.security.AuthoritiesConstants;
import com.myweather.myapp.service.WeatherService;
import com.myweather.myapp.service.dto.CitySearchDto;
import com.myweather.myapp.web.rest.vm.CitiesWeatherVM;
import java.text.ParseException;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WeatherResource {

    private final Logger log = LoggerFactory.getLogger(WeatherResource.class);

    @Inject
    WeatherService weatherService;

    @PostMapping("/citiesWeatherData")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<CitiesWeatherVM> readCityWeatherData(@Valid @RequestBody CitySearchDto citySearchDto) throws ParseException, JsonProcessingException {
        log.debug("REST request to read city weather data : {}", citySearchDto);

        return weatherService.getWeatherForThreeCities(citySearchDto);
    }
}
