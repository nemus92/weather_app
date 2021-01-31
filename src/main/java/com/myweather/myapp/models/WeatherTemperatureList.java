package com.myweather.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherTemperatureList {

    @JsonProperty("list")
    List<WeatherTemperature> list = new ArrayList<>();

    public WeatherTemperatureList() {}

    public WeatherTemperatureList(List<WeatherTemperature> list) {
        this.list = list;
    }

    public List<WeatherTemperature> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "WeatherTemperatureList{" +
            "list=" + list +
            '}';
    }
}