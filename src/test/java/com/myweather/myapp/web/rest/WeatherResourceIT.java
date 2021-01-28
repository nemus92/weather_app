package com.myweather.myapp.web.rest;

import com.myweather.myapp.WeatherAppApp;
import com.myweather.myapp.domain.Weather;
import com.myweather.myapp.repository.WeatherRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.myweather.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WeatherResource} REST controller.
 */
@SpringBootTest(classes = WeatherAppApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class WeatherResourceIT {

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final Double DEFAULT_FEELS_LIKE = 1D;
    private static final Double UPDATED_FEELS_LIKE = 2D;

    private static final Double DEFAULT_TEMPERATURE_MIN = 1D;
    private static final Double UPDATED_TEMPERATURE_MIN = 2D;

    private static final Double DEFAULT_TEMPERATURE_MAX = 1D;
    private static final Double UPDATED_TEMPERATURE_MAX = 2D;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeatherMockMvc;

    private Weather weather;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weather createEntity(EntityManager em) {
        Weather weather = new Weather()
            .temperature(DEFAULT_TEMPERATURE)
            .feelsLike(DEFAULT_FEELS_LIKE)
            .temperatureMin(DEFAULT_TEMPERATURE_MIN)
            .temperatureMax(DEFAULT_TEMPERATURE_MAX)
            .date(DEFAULT_DATE);
        return weather;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weather createUpdatedEntity(EntityManager em) {
        Weather weather = new Weather()
            .temperature(UPDATED_TEMPERATURE)
            .feelsLike(UPDATED_FEELS_LIKE)
            .temperatureMin(UPDATED_TEMPERATURE_MIN)
            .temperatureMax(UPDATED_TEMPERATURE_MAX)
            .date(UPDATED_DATE);
        return weather;
    }

    @BeforeEach
    public void initTest() {
        weather = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeather() throws Exception {
        int databaseSizeBeforeCreate = weatherRepository.findAll().size();
        // Create the Weather
        restWeatherMockMvc.perform(post("/api/weathers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weather)))
            .andExpect(status().isCreated());

        // Validate the Weather in the database
        List<Weather> weatherList = weatherRepository.findAll();
        assertThat(weatherList).hasSize(databaseSizeBeforeCreate + 1);
        Weather testWeather = weatherList.get(weatherList.size() - 1);
        assertThat(testWeather.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testWeather.getFeelsLike()).isEqualTo(DEFAULT_FEELS_LIKE);
        assertThat(testWeather.getTemperatureMin()).isEqualTo(DEFAULT_TEMPERATURE_MIN);
        assertThat(testWeather.getTemperatureMax()).isEqualTo(DEFAULT_TEMPERATURE_MAX);
        assertThat(testWeather.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createWeatherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weatherRepository.findAll().size();

        // Create the Weather with an existing ID
        weather.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeatherMockMvc.perform(post("/api/weathers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weather)))
            .andExpect(status().isBadRequest());

        // Validate the Weather in the database
        List<Weather> weatherList = weatherRepository.findAll();
        assertThat(weatherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWeathers() throws Exception {
        // Initialize the database
        weatherRepository.saveAndFlush(weather);

        // Get all the weatherList
        restWeatherMockMvc.perform(get("/api/weathers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weather.getId().intValue())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].feelsLike").value(hasItem(DEFAULT_FEELS_LIKE.doubleValue())))
            .andExpect(jsonPath("$.[*].temperatureMin").value(hasItem(DEFAULT_TEMPERATURE_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].temperatureMax").value(hasItem(DEFAULT_TEMPERATURE_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getWeather() throws Exception {
        // Initialize the database
        weatherRepository.saveAndFlush(weather);

        // Get the weather
        restWeatherMockMvc.perform(get("/api/weathers/{id}", weather.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weather.getId().intValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.feelsLike").value(DEFAULT_FEELS_LIKE.doubleValue()))
            .andExpect(jsonPath("$.temperatureMin").value(DEFAULT_TEMPERATURE_MIN.doubleValue()))
            .andExpect(jsonPath("$.temperatureMax").value(DEFAULT_TEMPERATURE_MAX.doubleValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }
    @Test
    @Transactional
    public void getNonExistingWeather() throws Exception {
        // Get the weather
        restWeatherMockMvc.perform(get("/api/weathers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeather() throws Exception {
        // Initialize the database
        weatherRepository.saveAndFlush(weather);

        int databaseSizeBeforeUpdate = weatherRepository.findAll().size();

        // Update the weather
        Weather updatedWeather = weatherRepository.findById(weather.getId()).get();
        // Disconnect from session so that the updates on updatedWeather are not directly saved in db
        em.detach(updatedWeather);
        updatedWeather
            .temperature(UPDATED_TEMPERATURE)
            .feelsLike(UPDATED_FEELS_LIKE)
            .temperatureMin(UPDATED_TEMPERATURE_MIN)
            .temperatureMax(UPDATED_TEMPERATURE_MAX)
            .date(UPDATED_DATE);

        restWeatherMockMvc.perform(put("/api/weathers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedWeather)))
            .andExpect(status().isOk());

        // Validate the Weather in the database
        List<Weather> weatherList = weatherRepository.findAll();
        assertThat(weatherList).hasSize(databaseSizeBeforeUpdate);
        Weather testWeather = weatherList.get(weatherList.size() - 1);
        assertThat(testWeather.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testWeather.getFeelsLike()).isEqualTo(UPDATED_FEELS_LIKE);
        assertThat(testWeather.getTemperatureMin()).isEqualTo(UPDATED_TEMPERATURE_MIN);
        assertThat(testWeather.getTemperatureMax()).isEqualTo(UPDATED_TEMPERATURE_MAX);
        assertThat(testWeather.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingWeather() throws Exception {
        int databaseSizeBeforeUpdate = weatherRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherMockMvc.perform(put("/api/weathers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weather)))
            .andExpect(status().isBadRequest());

        // Validate the Weather in the database
        List<Weather> weatherList = weatherRepository.findAll();
        assertThat(weatherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWeather() throws Exception {
        // Initialize the database
        weatherRepository.saveAndFlush(weather);

        int databaseSizeBeforeDelete = weatherRepository.findAll().size();

        // Delete the weather
        restWeatherMockMvc.perform(delete("/api/weathers/{id}", weather.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weather> weatherList = weatherRepository.findAll();
        assertThat(weatherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
