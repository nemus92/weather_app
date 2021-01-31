package com.myweather.myapp.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.auth.In;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

public class CitiesWeatherVM {

    @NotNull
    private Long cityId;

    @NotNull
    private String cityName;

    @NotNull
    private Integer cityOpenWeatherId;

    public CitiesWeatherVM(@NotNull Long cityId, @NotNull String cityName, @NotNull Integer cityOpenWeatherId) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityOpenWeatherId = cityOpenWeatherId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityOpenWeatherId() {
        return cityOpenWeatherId;
    }

    public void setCityOpenWeatherId(Integer cityOpenWeatherId) {
        this.cityOpenWeatherId = cityOpenWeatherId;
    }

    @Override
    public String toString() {
        return "CitiesWeatherVM{" +
            "cityId=" + cityId +
            ", cityName='" + cityName + '\'' +
            ", cityOpenWeatherId=" + cityOpenWeatherId +
            '}';
    }
}
