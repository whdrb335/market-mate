package com.example.market_mate.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignRequest {

    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String loginPw;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;

    @NotBlank(message = "가게명을 입력해주세요")
    private String storeName;

    @NotBlank(message = "시장명을 입력해주세요")
    private String marketName;
}