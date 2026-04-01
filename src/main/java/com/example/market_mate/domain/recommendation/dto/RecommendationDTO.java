package com.example.market_mate.domain.recommendation.dto;

import com.example.market_mate.domain.recommendation.entity.OrderRecommendation;
import com.example.market_mate.domain.sales.entity.Season;
import com.example.market_mate.domain.sales.entity.SpecialDay;
import com.example.market_mate.domain.sales.entity.Weather;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class RecommendationDTO {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String unit;
    private LocalDate recommendDate;
    private int baseQuantity;
    private double weatherMultiplier;
    private double seasonMultiplier;
    private double dayMultiplier;
    private double specialDayMultiplier;
    private int currentStock;
    private int recommendQuantity;
    private Weather expectedWeather;
    private Season season;
    private SpecialDay specialDay;
    private boolean isMarketDay;
    private LocalDateTime createdAt;

    public RecommendationDTO(OrderRecommendation rec) {
        this.id = rec.getId();
        this.userId = rec.getUser().getId();
        this.productId = rec.getProduct().getId();
        this.productName = rec.getProduct().getName();
        this.unit = rec.getProduct().getUnit();
        this.recommendDate = rec.getRecommendDate();
        this.baseQuantity = rec.getBaseQuantity();
        this.weatherMultiplier = rec.getWeatherMultiplier();
        this.seasonMultiplier = rec.getSeasonMultiplier();
        this.dayMultiplier = rec.getDayMultiplier();
        this.specialDayMultiplier = rec.getSpecialDayMultiplier();
        this.currentStock = rec.getCurrentStock();
        this.recommendQuantity = rec.getRecommendQuantity();
        this.expectedWeather = rec.getExpectedWeather();
        this.season = rec.getSeason();
        this.specialDay = rec.getSpecialDay();
        this.isMarketDay = rec.isMarketDay();
        this.createdAt = rec.getCreatedAt();
    }
}