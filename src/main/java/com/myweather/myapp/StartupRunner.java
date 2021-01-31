package com.myweather.myapp;

import com.myweather.myapp.service.CityService;
import com.myweather.myapp.service.WeatherService;
import javax.inject.Inject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    // TODO runner for executing service call on app startup, like reading cities from JSON and storing them in DB
    // Also a rest service exists for this

    // TODO used to using @Inject annotation, I know sping default is @Autowired
    @Inject
    CityService cityService;

    @Override
    public void run(String... args) throws Exception {
        cityService.readAndSaveCities();
    }
}
