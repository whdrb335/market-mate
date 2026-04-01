package com.example.market_mate.domain.sales.request;

import com.example.market_mate.domain.sales.entity.SpecialDay;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateSalesRecordRequest {

    @NotNull(message = "품목 ID를 입력해주세요")
    private Long productId;

    @NotNull(message = "판매일을 입력해주세요")
    private LocalDate salesDate;

    @NotNull(message = "판매량을 입력해주세요")
    private int quantity;

    // 날씨는 자동으로 가져오므로 optional
    private SpecialDay specialDay = SpecialDay.NONE;
}