package com.myweather.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityTemperature {

    @JsonProperty("main.temp")
    private Double currentTemperature;

    @JsonProperty("main.feels_like")
    private Double feelsLike;

    @JsonProperty("main.temp_min")
    private Double temperatureMin;

    @JsonProperty("main.temp_max")
    private Double temperatureMax;

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    @Override
    public String toString() {
        return "CityTemperature{" +
            "currentTemperature=" + currentTemperature +
            ", feelsLike=" + feelsLike +
            ", temperatureMin=" + temperatureMin +
            ", temperatureMax=" + temperatureMax +
            '}';
    }
}
