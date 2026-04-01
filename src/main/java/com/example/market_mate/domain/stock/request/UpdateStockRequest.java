package com.example.market_mate.domain.stock.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStockRequest {

    @NotNull(message = "수량을 입력해주세요")
    private int quantity;
}