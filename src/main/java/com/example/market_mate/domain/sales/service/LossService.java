package com.example.market_mate.domain.sales.service;

import com.example.market_mate.common.exception.product.NotFoundProductException;
import com.example.market_mate.common.exception.stock.NotEnoughStockException;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.repository.ProductRepository;
import com.example.market_mate.domain.sales.entity.LossRecord;
import com.example.market_mate.domain.sales.entity.LossReason;
import com.example.market_mate.domain.sales.repository.LossRecordRepository;
import com.example.market_mate.domain.stock.entity.Stock;
import com.example.market_mate.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LossService {

    private final LossRecordRepository lossRecordRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    /**
     * 손실 기록 등록
     */
    @Transactional
    public LossRecord createLossRecord(Long productId, LocalDate lossDate,
                                        int quantity, LossReason reason) {
        Product product = getProductById(productId);

        // 재고 차감
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotEnoughStockException(
                        "재고 정보가 없습니다.", HttpStatus.NOT_FOUND));
        stock.removeStock(quantity);

        LossRecord record = LossRecord.createLossRecord(
                product, lossDate, quantity, reason);
        return lossRecordRepository.save(record);
    }

    /**
     * 품목별 손실 기록 조회
     */
    public List<LossRecord> getLossRecordsByProduct(Long productId) {
        return lossRecordRepository.findByProductId(productId);
    }

    /**
     * 기간별 손실 기록 조회
     */
    public List<LossRecord> getLossRecordsByPeriod(Long productId,
                                                    LocalDate start, LocalDate end) {
        return lossRecordRepository.findByProductIdAndLossDateBetween(
                productId, start, end);
    }

    // == 공통 메서드 ==
    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException(
                        "존재하지 않는 품목입니다.", HttpStatus.NOT_FOUND));
    }
}