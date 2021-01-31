package com.myweather.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweather.myapp.domain.City;
import com.myweather.myapp.models.CityInfo;
import com.myweather.myapp.repository.CityRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CityService {

    @Inject
    CityRepository cityRepository;

    public void readAndSaveCities() {

        // TODO creating object mapper so I can read from json file, and map data to model (POJO), limited to 500 cities
        final ObjectMapper mapper = new ObjectMapper();

        try {
            final List<CityInfo> cities = Arrays.asList(mapper.readValue(new ClassPathResource("cities_json/city-list.json").getFile(), CityInfo[].class));

            final List<City> existingCities = cityRepository.findAll();

            // TODO check if id and name are present, if present check if already existis in DB, if not exists, persist
            cities.stream().limit(1000).filter(cityInfo -> Optional.ofNullable(cityInfo.getName()).isPresent() && Optional.ofNullable(cityInfo.getId()).isPresent())
                .map(cityInfo -> new City(cityInfo.getName(), cityInfo.getId())).filter(city -> !existingCities.contains(city))
                .forEach(city -> cityRepository.save(city));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
