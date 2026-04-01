package com.example.market_mate.common.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    // RestTemplate 직접 생성 (빈 주입 X)
    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherInfo getCurrentWeather() {
        try {
            String url = apiUrl + "/weather?q=Seoul&appid=" + apiKey + "&units=metric";
            Map response = restTemplate.getForObject(url, Map.class);

            Map weather = (Map) ((List) response.get("weather")).get(0);
            Map main = (Map) response.get("main");

            String weatherMain = (String) weather.get("main");
            double temperature = ((Number) main.get("temp")).doubleValue();

            return new WeatherInfo(convertWeather(weatherMain), temperature);
        } catch (Exception e) {
            log.error("날씨 API 호출 실패: {}", e.getMessage());
            return new WeatherInfo(
                    com.example.market_mate.domain.sales.entity.Weather.SUNNY, 20.0);
        }
    }

    private com.example.market_mate.domain.sales.entity.Weather convertWeather(
            String weatherMain) {
        return switch (weatherMain) {
            case "Rain", "Drizzle", "Thunderstorm" ->
                    com.example.market_mate.domain.sales.entity.Weather.RAINY;
            case "Snow" ->
                    com.example.market_mate.domain.sales.entity.Weather.SNOW;
            case "Clouds" ->
                    com.example.market_mate.domain.sales.entity.Weather.CLOUDY;
            default ->
                    com.example.market_mate.domain.sales.entity.Weather.SUNNY;
        };
    }
}