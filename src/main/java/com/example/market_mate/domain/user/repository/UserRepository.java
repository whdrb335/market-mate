package com.example.market_mate.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.market_mate.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}