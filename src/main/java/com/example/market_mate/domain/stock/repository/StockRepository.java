package com.example.market_mate.domain.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.market_mate.domain.stock.entity.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductId(Long productId);
}