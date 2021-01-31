package com.myweather.myapp.service.dto;

import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

public class WeatherCitySearchDto {

    private List<Long> cityIds;

    @NotNull
    private ZonedDateTime dateFrom;

    @NotNull
    private ZonedDateTime dateTo;

    public List<Long> getCityIds() {
        return cityIds;
    }

    public void setCityIds(List<Long> cityIds) {
        this.cityIds = cityIds;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(ZonedDateTime dateTo) {
        this.dateTo = dateTo;
    }


    @Override
    public String toString() {
        return "WeatherCitySearchDto{" +
            "cityIds=" + cityIds +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            '}';
    }
}
