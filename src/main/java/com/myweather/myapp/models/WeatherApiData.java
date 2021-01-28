package com.myweather.myapp.models;

// TODO made this so that the data would be read from yml, encapsulated and later injected in WeatherService, and used with setters and getters
public class WeatherApiData {

    private String weatherApiUrl;

    private String weatherApiKey;

    public String getWeatherApiKey() {
        return weatherApiKey;
    }

    public void setWeatherApiKey(String weatherApiKey) {
        this.weatherApiKey = weatherApiKey;
    }

    public String getWeatherApiUrl() {
        return weatherApiUrl;
    }

    public void setWeatherApiUrl(String weatherApiUrl) {
        this.weatherApiUrl = weatherApiUrl;
    }
}
