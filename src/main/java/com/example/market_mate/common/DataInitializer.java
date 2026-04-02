package com.example.market_mate.common;

import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        // 이미 데이터 있으면 스킵
        if (userRepository.count() > 0) return;

        User user = User.createUser(
                "kim123",
                "1234",
                "김종규",
                "010-3582-9211",
                "영양농산물",
                "신매시장",
                bCryptPasswordEncoder
        );
        userRepository.save(user);
    }
}