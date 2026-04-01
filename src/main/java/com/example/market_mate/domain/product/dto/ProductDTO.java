package com.example.market_mate.domain.product.dto;

import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.entity.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductDTO {

    private Long id;
    private Long userId;
    private String name;
    private String unit;
    private int purchasePrice;
    private int sellingPrice;
    private ProductStatus status;
    private LocalDateTime createdAt;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.userId = product.getUser().getId();
        this.name = product.getName();
        this.unit = product.getUnit();
        this.purchasePrice = product.getPurchasePrice();
        this.sellingPrice = product.getSellingPrice();
        this.status = product.getStatus();
        this.createdAt = product.getCreatedAt();
    }
}