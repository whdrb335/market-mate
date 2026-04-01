package com.example.market_mate.domain.user.entity;

import com.example.market_mate.common.exception.user.NotMatchPasswd;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String loginPw;
    private String name;
    private String phone;
    private String storeName;   // 가게명
    private String marketName;  // 시장명
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 생성 메서드
    public static User createUser(String loginId, String loginPw, String name,
                                   String phone, String storeName, String marketName,
                                   BCryptPasswordEncoder encoder) {
        User user = new User();
        user.loginId = loginId;
        user.loginPw = encoder.encode(loginPw);
        user.name = name;
        user.phone = phone;
        user.storeName = storeName;
        user.marketName = marketName;
        user.role = UserRole.USER;
        user.status = UserStatus.ACTIVE;
        return user;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void isMatchPasswd(String passwd, BCryptPasswordEncoder encoder) {
        if (!encoder.matches(passwd, this.loginPw)) {
            throw new NotMatchPasswd("비밀번호가 맞지 않습니다", HttpStatus.BAD_REQUEST);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}