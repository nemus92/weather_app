package com.myweather.myapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class WeatherTemperature {

    @JsonProperty("dt")
    private ZonedDateTime date;

    @JsonProperty("main")
    private WeatherData main;

    public WeatherData getMain() {
        return main;
    }

    public void setMain(WeatherData main) {
        this.main = main;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "WeatherTemperature{" +
            "date=" + date +
            ", main=" + main +
            '}';
    }
}
