package com.example.market_mate.domain.sales.dto;

import com.example.market_mate.domain.sales.entity.LossRecord;
import com.example.market_mate.domain.sales.entity.LossReason;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class LossRecordDTO {

    private Long id;
    private Long productId;
    private String productName;
    private LocalDate lossDate;
    private int quantity;
    private LossReason reason;
    private LocalDateTime createdAt;

    public LossRecordDTO(LossRecord record) {
        this.id = record.getId();
        this.productId = record.getProduct().getId();
        this.productName = record.getProduct().getName();
        this.lossDate = record.getLossDate();
        this.quantity = record.getQuantity();
        this.reason = record.getReason();
        this.createdAt = record.getCreatedAt();
    }
}