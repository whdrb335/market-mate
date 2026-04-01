package com.example.market_mate.domain.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String loginId;
    private String role;
    private String accessToken;
    private String refreshToken;
}