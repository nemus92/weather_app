package com.myweather.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityId {

    @JsonProperty("id")
    private Integer cityId;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "CityId{" +
            "cityId=" + cityId +
            '}';
    }
}
