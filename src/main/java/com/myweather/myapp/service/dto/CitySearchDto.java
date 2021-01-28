package com.myweather.myapp.service.dto;

import java.util.List;

public class CitySearchDto {

    private List<String> cities;

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
