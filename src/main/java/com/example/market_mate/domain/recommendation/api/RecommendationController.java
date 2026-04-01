package com.example.market_mate.domain.recommendation.api;

import com.example.market_mate.common.Result;
import com.example.market_mate.domain.recommendation.dto.RecommendationDTO;
import com.example.market_mate.domain.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
@Tag(name = "Recommendation API", description = "발주 추천 API")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "내일 발주 추천 생성")
    @PostMapping("/generate")
    public Result<List<RecommendationDTO>> generateRecommendations(
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<RecommendationDTO> recommendations = recommendationService
                .generateRecommendations(userId)
                .stream()
                .map(RecommendationDTO::new)
                .toList();
        return new Result<>(recommendations);
    }

    @Operation(summary = "날짜별 추천 조회")
    @GetMapping
    public Result<List<RecommendationDTO>> getRecommendations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<RecommendationDTO> recommendations = recommendationService
                .getRecommendations(userId, date)
                .stream()
                .map(RecommendationDTO::new)
                .toList();
        return new Result<>(recommendations);
    }
}