package com.example.market_mate.domain.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductRequest {

    @NotBlank(message = "품목명을 입력해주세요")
    private String name;

    @NotBlank(message = "단위를 입력해주세요")
    private String unit;

    @NotNull(message = "매입가를 입력해주세요")
    private int purchasePrice;

    @NotNull(message = "판매가를 입력해주세요")
    private int sellingPrice;
}