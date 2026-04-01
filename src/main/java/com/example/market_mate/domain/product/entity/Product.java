package com.example.market_mate.domain.product.entity;

import com.example.market_mate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;        // 품목명 (감자, 양파 등)
    private String unit;        // 단위 (kg, 개)
    private int purchasePrice;  // 매입가
    private int sellingPrice;   // 판매가

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product createProduct(User user, String name, String unit,
                                         int purchasePrice, int sellingPrice) {
        Product product = new Product();
        product.user = user;
        product.name = name;
        product.unit = unit;
        product.purchasePrice = purchasePrice;
        product.sellingPrice = sellingPrice;
        product.status = ProductStatus.ACTIVE;
        return product;
    }

    public void updateProduct(String name, String unit,
                               int purchasePrice, int sellingPrice) {
        this.name = name;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
    }

    public void delete() {
        this.status = ProductStatus.DELETE;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
