package com.myweather.myapp.repository.impl;

import com.myweather.myapp.domain.Weather;
import com.myweather.myapp.repository.WeatherRepository;
import com.myweather.myapp.repository.WeatherRepositoryCustom;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class WeatherRepositoryImpl implements WeatherRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Weather> getWeatherDataForDates(ZonedDateTime dateFrom, ZonedDateTime dateTo,
        List<Long> cityIds) {

        if (cityIds.isEmpty()) {
            return entityManager.createQuery("SELECT w FROM Weather w WHERE w.date BETWEEN :dateFrom AND :dateTo", Weather.class)
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
        } else {
            return entityManager.createQuery("SELECT w FROM Weather w WHERE w.date BETWEEN :dateFrom AND :dateTo AND w.city.id in :cityIds", Weather.class)
                .setParameter("cityIds", cityIds)
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
        }
    }
}
