package com.example.market_mate.domain.recommendation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.sales.entity.Season;
import com.example.market_mate.domain.sales.entity.SpecialDay;
import com.example.market_mate.domain.sales.entity.Weather;
import com.example.market_mate.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRecommendation {

    @Id @GeneratedValue
    @Column(name = "recommendation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDate recommendDate;        // 추천일
    private int baseQuantity;               // 기준 평균 판매량
    private double weatherMultiplier;       // 날씨 배수
    private double seasonMultiplier;        // 계절 배수
    private double dayMultiplier;           // 요일/장날 배수
    private double specialDayMultiplier;    // 명절 배수
    private int currentStock;              // 현재 재고
    private int recommendQuantity;          // 최종 추천 발주량

    @Enumerated(EnumType.STRING)
    private Weather expectedWeather;        // 예상 날씨

    @Enumerated(EnumType.STRING)
    private Season season;

    @Enumerated(EnumType.STRING)
    private SpecialDay specialDay;

    private boolean isMarketDay;

    private LocalDateTime createdAt;

    public static OrderRecommendation createRecommendation(
            User user, Product product, LocalDate recommendDate,
            int baseQuantity, double weatherMultiplier, double seasonMultiplier,
            double dayMultiplier, double specialDayMultiplier,
            int currentStock, Weather expectedWeather,
            Season season, SpecialDay specialDay, boolean isMarketDay) {

        OrderRecommendation rec = new OrderRecommendation();
        rec.user = user;
        rec.product = product;
        rec.recommendDate = recommendDate;
        rec.baseQuantity = baseQuantity;
        rec.weatherMultiplier = weatherMultiplier;
        rec.seasonMultiplier = seasonMultiplier;
        rec.dayMultiplier = dayMultiplier;
        rec.specialDayMultiplier = specialDayMultiplier;
        rec.currentStock = currentStock;
        rec.expectedWeather = expectedWeather;
        rec.season = season;
        rec.specialDay = specialDay;
        rec.isMarketDay = isMarketDay;

        // 최종 추천 발주량 계산
        rec.recommendQuantity = (int) Math.max(0,
            Math.round(baseQuantity
                * weatherMultiplier
                * seasonMultiplier
                * dayMultiplier
                * specialDayMultiplier)
            - currentStock);

        return rec;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}