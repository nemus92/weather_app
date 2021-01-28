package com.myweather.myapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweather.myapp.models.CityId;
import com.myweather.myapp.models.CityTemperature;
import com.myweather.myapp.service.dto.CitySearchDto;
import com.myweather.myapp.models.WeatherApiData;
import com.myweather.myapp.web.rest.vm.CitiesWeatherVM;
import java.text.ParseException;
import java.util.List;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class WeatherService {

    @Inject RestTemplate restTemplate;

    @Inject WeatherApiData weatherAppData;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<CitiesWeatherVM> getWeatherForThreeCities(CitySearchDto city) throws ParseException, JsonProcessingException {

        UriComponents uriComponents = UriComponentsBuilder
            .newInstance()
            .scheme("http")
            .host(weatherAppData.getWeatherApiUrl())
            .path("")
            .query("q={city}&appid={appid}")
            .buildAndExpand(city.getCities().get(0), weatherAppData.getWeatherApiKey());

        String uri = uriComponents.toUriString();
        System.out.println("uri " + uri.toString());

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

        System.out.println(response);

        ObjectMapper objectMapper = new ObjectMapper();

        CityId cityId = objectMapper.readValue(response.getBody(), CityId.class);

        CityTemperature cityTemperature = objectMapper.readValue(response.getBody(), CityTemperature.class);

        System.out.println("CITY ID " + cityId.toString());

        System.out.println("CITY TEMPERATURE " + cityTemperature.toString());

//        String description = null;
//        double temp=0;
//        int pressure=0;
//        int humidity = 0;
//        int temp_min=0;
//        int temp_max=0;
//        int temp_kf=0;
//        int sea_level=0;
//        int grnd_level=0;
//
//        java.util.Date date1 = null;
//
//        String date = null;
//
//        String icon=null;
//        String WeatherCondition=null;
//        int id=0;
//
//        JSONObject root = new JSONObject(result);
//
//        JSONArray weatherObject = root.getJSONArray("list");
//
//        for(int i = 0; i < weatherObject.length(); i++) {
//
//            JSONObject arrayElement = weatherObject.getJSONObject(i);
//
//            JSONObject main = arrayElement.getJSONObject("main");
//            temp = (int) main.getFloat("temp");
//            pressure =  main.getInt("pressure");
//            humidity = main.getInt("humidity");
//            temp_min = main.getInt("temp_min");
//            temp_max = main.getInt("temp_max");
//            temp_kf = main.getInt("temp_kf");
//            sea_level = main.getInt("sea_level");
//            grnd_level = main.getInt("grnd_level");
//
//            description = arrayElement.getJSONArray("weather").getJSONObject(0).getString("description");
//            icon = arrayElement.getJSONArray("weather").getJSONObject(0).getString("icon");
//            WeatherCondition = arrayElement.getJSONArray("weather").getJSONObject(0).getString("main");
//            id = arrayElement.getJSONArray("weather").getJSONObject(0).getInt("id");
//
//        }

        return null;
    }

}
