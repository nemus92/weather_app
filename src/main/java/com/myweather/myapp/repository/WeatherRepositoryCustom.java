package com.myweather.myapp.repository;

import com.myweather.myapp.domain.Weather;
import java.time.ZonedDateTime;
import java.util.List;

// TODO custom repository for, so that I can implement custom sql with entity manager
public interface WeatherRepositoryCustom {

    List<Weather> getWeatherDataForDates(ZonedDateTime dateFrom, ZonedDateTime dateTo, List<Long> cityIds);
}
