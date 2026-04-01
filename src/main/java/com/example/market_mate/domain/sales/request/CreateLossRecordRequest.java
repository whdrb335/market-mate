package com.example.market_mate.domain.sales.request;

import com.example.market_mate.domain.sales.entity.LossReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateLossRecordRequest {

    @NotNull(message = "품목 ID를 입력해주세요")
    private Long productId;

    @NotNull(message = "손실일을 입력해주세요")
    private LocalDate lossDate;

    @NotNull(message = "손실량을 입력해주세요")
    private int quantity;

    @NotNull(message = "손실 이유를 입력해주세요")
    private LossReason reason;
}