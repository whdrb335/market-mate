package com.example.market_mate.domain.sales.entity;

import com.example.market_mate.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LossRecord {

    @Id @GeneratedValue
    @Column(name = "loss_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDate lossDate;     // 손실일
    private int quantity;           // 손실량

    @Enumerated(EnumType.STRING)
    private LossReason reason;      // 손실 이유

    private LocalDateTime createdAt;

    public static LossRecord createLossRecord(Product product, LocalDate lossDate,
                                               int quantity, LossReason reason) {
        LossRecord record = new LossRecord();
        record.product = product;
        record.lossDate = lossDate;
        record.quantity = quantity;
        record.reason = reason;
        return record;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}