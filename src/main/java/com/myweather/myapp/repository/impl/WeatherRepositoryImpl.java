package com.myweather.myapp.repository.impl;

import com.myweather.myapp.domain.Weather;
import com.myweather.myapp.repository.WeatherRepository;
import com.myweather.myapp.repository.WeatherRepositoryCustom;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import liquibase.pro.packaged.S;

public class WeatherRepositoryImpl implements WeatherRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Weather> getWeatherDataForDates(ZonedDateTime dateFrom, ZonedDateTime dateTo,
        List<Long> cityIds) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT w FROM Weather w WHERE w.date BETWEEN :dateFrom AND :dateTo");

        if (cityIds.isEmpty()) {
            return entityManager.createQuery(sb.toString(), Weather.class)
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
        } else {
            sb.append(" AND w.city.id in :cityIds");
            return entityManager.createQuery(sb.toString(), Weather.class)
                .setParameter("cityIds", cityIds)
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
        }
    }
}
