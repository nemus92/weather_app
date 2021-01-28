package com.myweather.myapp.service;

import com.myweather.myapp.domain.City;
import com.myweather.myapp.repository.CityRepository;
import javax.inject.Inject;

public class CityService {

    @Inject
    CityRepository cityRepository;

    public void saveCities() {

        final City cityOne = new City();
        final City cityTwo = new City();
        final City cityThee = new City();

        cityOne.setName("Novi Sad");
        cityOne.setOpenWeatherId(111);

        cityOne.setName("Beograd");
        cityOne.setOpenWeatherId(111);

        cityOne.setName("Pariz");
        cityOne.setOpenWeatherId(111);

        cityRepository.save(cityOne);
        cityRepository.save(cityTwo);
        cityRepository.save(cityThee);
    }
}
