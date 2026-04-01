package com.example.market_mate.domain.user.dto;

import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.entity.UserRole;
import com.example.market_mate.domain.user.entity.UserStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDTO {

    private Long id;
    private String loginId;
    private String name;
    private String phone;
    private String storeName;
    private String marketName;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.storeName = user.getStoreName();
        this.marketName = user.getMarketName();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
    }
}