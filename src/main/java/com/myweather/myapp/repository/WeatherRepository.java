package com.myweather.myapp.repository;

import com.myweather.myapp.domain.Weather;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Weather entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w WHERE w.date BETWEEN :dateFrom AND :dateTo AND w.city.id in :cityIds")
    List<Weather> getWeatherDataForDates(@Param("dateFrom") ZonedDateTime dateFrom, @Param("dateTo") ZonedDateTime dateTo, @Param("cityIds") List<Long> cityIds);
}
