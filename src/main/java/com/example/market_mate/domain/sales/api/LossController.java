package com.example.market_mate.domain.sales.api;

import com.example.market_mate.common.Result;
import com.example.market_mate.domain.sales.dto.LossRecordDTO;
import com.example.market_mate.domain.sales.entity.LossRecord;
import com.example.market_mate.domain.sales.request.CreateLossRecordRequest;
import com.example.market_mate.domain.sales.service.LossService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loss")
@Tag(name = "Loss API", description = "손실 기록 관리 API")
public class LossController {

    private final LossService lossService;

    @Operation(summary = "손실 기록 등록")
    @PostMapping
    public Result<LossRecordDTO> createLossRecord(
            @Validated @RequestBody CreateLossRecordRequest request) {
        LossRecord record = lossService.createLossRecord(
                request.getProductId(), request.getLossDate(),
                request.getQuantity(), request.getReason()
        );
        return new Result<>(new LossRecordDTO(record));
    }

    @Operation(summary = "품목별 손실 기록 조회")
    @GetMapping("/product/{productId}")
    public Result<List<LossRecordDTO>> getLossRecordsByProduct(
            @PathVariable Long productId) {
        List<LossRecordDTO> records = lossService.getLossRecordsByProduct(productId)
                .stream()
                .map(LossRecordDTO::new)
                .toList();
        return new Result<>(records);
    }

    @Operation(summary = "기간별 손실 기록 조회")
    @GetMapping("/product/{productId}/period")
    public Result<List<LossRecordDTO>> getLossRecordsByPeriod(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<LossRecordDTO> records = lossService.getLossRecordsByPeriod(
                productId, start, end)
                .stream()
                .map(LossRecordDTO::new)
                .toList();
        return new Result<>(records);
    }
}