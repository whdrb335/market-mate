package com.example.market_mate.domain.sales.dto;

import com.example.market_mate.domain.sales.entity.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SalesRecordDTO {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private LocalDate salesDate;
    private int quantity;
    private Weather weather;
    private double temperature;
    private Season season;
    private boolean isMarketDay;
    private SpecialDay specialDay;
    private LocalDateTime createdAt;

    public SalesRecordDTO(SalesRecord record) {
        this.id = record.getId();
        this.userId = record.getUser().getId();
        this.productId = record.getProduct().getId();
        this.productName = record.getProduct().getName();
        this.salesDate = record.getSalesDate();
        this.quantity = record.getQuantity();
        this.weather = record.getWeather();
        this.temperature = record.getTemperature();
        this.season = record.getSeason();
        this.isMarketDay = record.isMarketDay();
        this.specialDay = record.getSpecialDay();
        this.createdAt = record.getCreatedAt();
    }
}