package com.example.market_mate.domain.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.market_mate.domain.sales.entity.SalesRecord;

import java.time.LocalDate;
import java.util.List;

public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    List<SalesRecord> findByUserIdAndSalesDateBetween(
        Long userId, LocalDate start, LocalDate end);
    List<SalesRecord> findByProductId(Long productId);
}