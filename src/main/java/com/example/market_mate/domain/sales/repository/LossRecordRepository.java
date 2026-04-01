package com.example.market_mate.domain.sales.repository;

import com.example.market_mate.domain.sales.entity.LossRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LossRecordRepository extends JpaRepository<LossRecord, Long> {
    List<LossRecord> findByProductId(Long productId);
    List<LossRecord> findByProductIdAndLossDateBetween(
            Long productId, LocalDate start, LocalDate end);
}