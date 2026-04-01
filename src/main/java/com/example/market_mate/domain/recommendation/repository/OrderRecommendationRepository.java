package com.example.market_mate.domain.recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.market_mate.domain.recommendation.entity.OrderRecommendation;

import java.time.LocalDate;
import java.util.List;

public interface OrderRecommendationRepository extends JpaRepository<OrderRecommendation, Long> {
    List<OrderRecommendation> findByUserIdAndRecommendDate(
        Long userId, LocalDate recommendDate);
}