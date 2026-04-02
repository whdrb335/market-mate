package com.example.market_mate.domain.recommendation.service;

import com.example.market_mate.common.exception.product.NotFoundProductException;
import com.example.market_mate.common.exception.user.NotFoundUserException;
import com.example.market_mate.common.weather.WeatherInfo;
import com.example.market_mate.common.weather.WeatherService;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.entity.ProductStatus;
import com.example.market_mate.domain.product.repository.ProductRepository;
import com.example.market_mate.domain.recommendation.entity.OrderRecommendation;
import com.example.market_mate.domain.recommendation.repository.OrderRecommendationRepository;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendationService {

    private final OrderRecommendationRepository recommendationRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final SalesRecordRepository salesRecordRepository;
    private final UserRepository userRepository;
    private final WeatherService weatherService;

    /**
     * 내일 발주 추천 생성
     */
    @Transactional
    public List<OrderRecommendation> generateRecommendations(Long userId) {
        User user = getUserById(userId);
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Product> products = productRepository
                .findByUserIdAndStatus(userId, ProductStatus.ACTIVE);

        return products.stream()
                .map(product -> generateRecommendation(user, product, tomorrow))
                .toList();
    }

    /**
     * 날짜별 추천 조회
     */
    public List<OrderRecommendation> getRecommendations(Long userId, LocalDate date) {
        return recommendationRepository.findByUserIdAndRecommendDate(userId, date);
    }

    // == 추천 계산 로직 ==
    private OrderRecommendation generateRecommendation(
            User user, Product product, LocalDate date) {

        // ⭐ 이미 해당 날짜에 해당 품목 추천이 있으면 기존 거 반환
        List<OrderRecommendation> existing =
                recommendationRepository.findByUserIdAndRecommendDate(user.getId(), date);

        Optional<OrderRecommendation> alreadyExists = existing.stream()
                .filter(r -> r.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (alreadyExists.isPresent()) {
            return alreadyExists.get();
        }

        // 1. 최근 30일 평균 판매량 계산
        LocalDate thirtyDaysAgo = date.minusDays(30);
        List<SalesRecord> recentSales = salesRecordRepository
                .findByProductId(product.getId())
                .stream()
                .filter(r -> r.getSalesDate().isAfter(thirtyDaysAgo))
                .toList();

        int baseQuantity = recentSales.isEmpty() ? 10 :
                (int) recentSales.stream()
                        .mapToInt(SalesRecord::getQuantity)
                        .average()
                        .orElse(10);

        // 2. 날씨 API 연동
        WeatherInfo weatherInfo = weatherService.getCurrentWeather();
        Weather expectedWeather = weatherInfo.getWeather();
        double weatherMultiplier = getWeatherMultiplier(expectedWeather, product.getName());

        // 3. 계절 배수
        Season season = getSeason(date.getMonth());
        double seasonMultiplier = 1.0;

        // 4. 요일/장날 배수
        boolean isMarketDay = date.getDayOfWeek() == DayOfWeek.THURSDAY;
        double dayMultiplier = isMarketDay ? 2.0 : 1.0;

        // 5. 명절 배수
        SpecialDay specialDay = SpecialDay.NONE;
        double specialDayMultiplier = 1.0;

        // 6. 현재 재고
        int currentStock = stockRepository.findByProductId(product.getId())
                .map(Stock::getQuantity)
                .orElse(0);

        return recommendationRepository.save(
                OrderRecommendation.createRecommendation(
                        user, product, date,
                        baseQuantity, weatherMultiplier, seasonMultiplier,
                        dayMultiplier, specialDayMultiplier,
                        currentStock, expectedWeather, season,
                        specialDay, isMarketDay));
    }

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

    private double getWeatherMultiplier(Weather weather, String productName) {
        if (weather == Weather.RAINY) {
            if (productName.contains("정구지") || productName.contains("부추")) {
                return 1.3;
            }
            return 0.7;
        }
        if (weather == Weather.SNOW) {
            return 0.5;
        }
        return 1.0;
    }
}