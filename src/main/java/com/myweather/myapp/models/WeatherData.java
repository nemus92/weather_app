package com.myweather.myapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherData {

    @JsonProperty("temp")
    private Integer temp;

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
            "temp=" + temp +
            '}';
    }
}
