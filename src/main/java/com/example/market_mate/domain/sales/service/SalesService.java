package com.example.market_mate.domain.sales.service;

import com.example.market_mate.common.exception.product.NotFoundProductException;
import com.example.market_mate.common.exception.stock.NotEnoughStockException;
import com.example.market_mate.common.exception.user.NotFoundUserException;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.repository.ProductRepository;
import com.example.market_mate.domain.sales.entity.*;
import com.example.market_mate.domain.sales.repository.SalesRecordRepository;
import com.example.market_mate.domain.stock.entity.Stock;
import com.example.market_mate.domain.stock.repository.StockRepository;
import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesService {

    private final SalesRecordRepository salesRecordRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    /**
     * 판매 기록 등록
     */
    @Transactional
    public SalesRecord createSalesRecord(Long userId, Long productId,
                                          LocalDate salesDate, int quantity,
                                          SpecialDay specialDay) {
        User user = getUserById(userId);
        Product product = getProductById(productId);

        // 재고 차감
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotEnoughStockException(
                        "재고 정보가 없습니다.", HttpStatus.NOT_FOUND));
        stock.removeStock(quantity);

        // 날씨/계절/장날 자동 계산
        Weather weather = Weather.SUNNY; // 추후 날씨 API 연동
        double temperature = 20.0;       // 추후 날씨 API 연동
        Season season = getSeason(salesDate.getMonth());
        boolean isMarketDay = salesDate.getDayOfWeek() == DayOfWeek.THURSDAY;

        SalesRecord record = SalesRecord.createSalesRecord(
                user, product, salesDate, quantity,
                weather, temperature, season, isMarketDay, specialDay);

        return salesRecordRepository.save(record);
    }

    /**
     * 내 판매 기록 조회 (기간별)
     */
    public List<SalesRecord> getMySalesRecords(Long userId,
                                                LocalDate start, LocalDate end) {
        return salesRecordRepository.findByUserIdAndSalesDateBetween(
                userId, start, end);
    }

    /**
     * 품목별 판매 기록 조회
     */
    public List<SalesRecord> getSalesRecordsByProduct(Long productId) {
        return salesRecordRepository.findByProductId(productId);
    }

    // == 공통 메서드 ==
    private Season getSeason(Month month) {
        return switch (month) {
            case MARCH, APRIL, MAY -> Season.SPRING;
            case JUNE, JULY, AUGUST -> Season.SUMMER;
            case SEPTEMBER, OCTOBER, NOVEMBER -> Season.FALL;
            default -> Season.WINTER;
        };
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(
                        "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException(
                        "존재하지 않는 품목입니다.", HttpStatus.NOT_FOUND));
    }
}