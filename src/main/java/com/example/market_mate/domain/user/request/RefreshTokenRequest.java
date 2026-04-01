package com.example.market_mate.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    @NotBlank(message = "RefreshToken을 입력해주세요")
    private String refreshToken;
}