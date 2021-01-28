package com.myweather.myapp.config;

import com.myweather.myapp.models.WeatherApiData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherDataConfiguration {

    @Value("${weather.url}")
    private String openWeatherApiUrl;

    @Value("${weather.api_key}")
    private String openWeatherApiKey;

    // TODO

    @Bean
    public WeatherApiData weatherApiData() {

        WeatherApiData weatherApiData = new WeatherApiData();
        weatherApiData.setWeatherApiUrl(openWeatherApiUrl);
        weatherApiData.setWeatherApiKey(openWeatherApiKey);

        return weatherApiData;
    }
}
