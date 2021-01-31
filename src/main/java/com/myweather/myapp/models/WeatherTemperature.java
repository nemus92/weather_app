package com.myweather.myapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherTemperature {

    @JsonProperty("main")
    private WeatherData main;

    public WeatherData getMain() {
        return main;
    }

    public void setMain(WeatherData main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "WeatherTemperature{" +
            "main=" + main +
            '}';
    }
}
