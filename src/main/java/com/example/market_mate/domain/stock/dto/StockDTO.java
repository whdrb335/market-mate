package com.example.market_mate.domain.stock.dto;

import com.example.market_mate.domain.stock.entity.Stock;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StockDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String unit;
    private int quantity;
    private LocalDateTime updatedAt;

    public StockDTO(Stock stock) {
        this.id = stock.getId();
        this.productId = stock.getProduct().getId();
        this.productName = stock.getProduct().getName();
        this.unit = stock.getProduct().getUnit();
        this.quantity = stock.getQuantity();
        this.updatedAt = stock.getUpdatedAt();
    }
}