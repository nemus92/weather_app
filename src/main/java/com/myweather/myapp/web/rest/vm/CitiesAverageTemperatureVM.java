package com.myweather.myapp.web.rest.vm;

import javax.validation.constraints.NotNull;

public class CitiesAverageTemperatureVM {

    @NotNull
    private Long cityId;

    @NotNull
    private String cityName;

    @NotNull
    private Double averageTemperature;

    public CitiesAverageTemperatureVM(@NotNull Long cityId, @NotNull String cityName, @NotNull Double averageTemperature) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.averageTemperature = averageTemperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "CitiesAverageTemperatureVM{" +
            "cityId=" + cityId +
            ", cityName='" + cityName + '\'' +
            ", averageTemperature=" + averageTemperature +
            '}';
    }
}
