package com.example.market_mate.domain.sales.entity;

import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesRecord {

    @Id @GeneratedValue
    @Column(name = "sales_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDate salesDate;    // 판매일
    private int quantity;           // 판매량

    @Enumerated(EnumType.STRING)
    private Weather weather;        // 날씨

    private double temperature;     // 기온

    @Enumerated(EnumType.STRING)
    private Season season;          // 계절

    private boolean isMarketDay;    // 장날 여부 (목요일)

    @Enumerated(EnumType.STRING)
    private SpecialDay specialDay;  // 명절 여부

    private LocalDateTime createdAt;

    public static SalesRecord createSalesRecord(User user, Product product,
                                                 LocalDate salesDate, int quantity,
                                                 Weather weather, double temperature,
                                                 Season season, boolean isMarketDay,
                                                 SpecialDay specialDay) {
        SalesRecord record = new SalesRecord();
        record.user = user;
        record.product = product;
        record.salesDate = salesDate;
        record.quantity = quantity;
        record.weather = weather;
        record.temperature = temperature;
        record.season = season;
        record.isMarketDay = isMarketDay;
        record.specialDay = specialDay;
        return record;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}