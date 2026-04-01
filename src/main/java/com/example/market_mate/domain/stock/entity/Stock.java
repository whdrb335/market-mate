package com.example.market_mate.domain.stock.entity;

import com.example.market_mate.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id @GeneratedValue
    @Column(name = "stock_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;       // 현재 재고량

    private LocalDateTime updatedAt;

    public static Stock createStock(Product product, int quantity) {
        Stock stock = new Stock();
        stock.product = product;
        stock.quantity = quantity;
        return stock;
    }

    // 재고 증가 (발주)
    public void addStock(int quantity) {
        this.quantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // 재고 감소 (판매)
    public void removeStock(int quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        this.quantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}