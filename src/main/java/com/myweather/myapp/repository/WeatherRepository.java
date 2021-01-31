package com.myweather.myapp.repository;

import com.myweather.myapp.domain.Weather;

import com.myweather.myapp.repository.impl.WeatherRepositoryImpl;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Weather entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>, WeatherRepositoryCustom {

}
