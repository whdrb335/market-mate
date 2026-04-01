package com.example.market_mate.common.weather;

import com.example.market_mate.domain.sales.entity.Weather;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherInfo {
    private Weather weather;
    private double temperature;
}