package com.library.spring.web.controller;

import com.library.spring.util.HttpClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Value("${openweather.host}")
    private String host;

    @Value("${openweather.key}")
    private String apiKey;

    private static final String BASE_PATH = "/data/2.5";

    @GetMapping("/country/{country}/city/{city}")
    @ApiOperation(value = "Get City Weather", notes = "Retrieve weather for city by name.", response = String.class)
    public String getWeather(@ApiParam(required = true, name = "city", value = "Name of the City") @PathVariable String city,
                             @ApiParam(required = true, name = "country", value = "Name of Country of the city") @PathVariable String country) throws Exception {
        HttpClient client = new HttpClient();
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", String.join(",", city, country));
        String uri = buildUri("/weather", queryParams);
        return client.get(uri);
    }

    @GetMapping("/forecast/country/{country}/city/{city}")
    @ApiOperation(value = "Get Weather Forecast", notes = "Retrieve 5 day or 3 hour forecast for city by name.", response = String.class)
    public String getForecast(@ApiParam(required = true, name = "city", value = "Name of the City") @PathVariable String city,
                              @ApiParam(required = true, name = "country", value = "Name of Country of the city") @PathVariable String country) throws Exception {

        HttpClient client = new HttpClient();
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("q", String.join(",", city, country));
        String uri = buildUri("/forecast", queryParams);
        return client.get(uri);
    }

    private String buildUri(String path, MultiValueMap<String, String> queryParams, Object... pathParams) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                            .scheme("http")
                                            .host(host)
                                            .path(BASE_PATH)
                                            .path(path)
                                            .queryParams(queryParams)
                                            .queryParam("appid", apiKey)
                                            .build()
                                            .expand(pathParams)
                                            .encode();
        return uriComponents.toUriString();
    }
}