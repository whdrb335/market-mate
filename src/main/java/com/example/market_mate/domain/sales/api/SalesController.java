package com.example.market_mate.domain.sales.api;

import com.example.market_mate.common.Result;
import com.example.market_mate.domain.sales.dto.SalesRecordDTO;
import com.example.market_mate.domain.sales.entity.SalesRecord;
import com.example.market_mate.domain.sales.request.CreateSalesRecordRequest;
import com.example.market_mate.domain.sales.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
@Tag(name = "Sales API", description = "판매 기록 관리 API")
public class SalesController {

    private final SalesService salesService;

    @Operation(summary = "판매 기록 등록")
    @PostMapping
    public Result<SalesRecordDTO> createSalesRecord(
            @Validated @RequestBody CreateSalesRecordRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        SalesRecord record = salesService.createSalesRecord(
                userId, request.getProductId(),
                request.getSalesDate(), request.getQuantity(),
                request.getSpecialDay()
        );
        return new Result<>(new SalesRecordDTO(record));
    }

    @Operation(summary = "판매 기록 조회 (기간별)")
    @GetMapping
    public Result<List<SalesRecordDTO>> getMySalesRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<SalesRecordDTO> records = salesService.getMySalesRecords(userId, start, end)
                .stream()
                .map(SalesRecordDTO::new)
                .toList();
        return new Result<>(records);
    }

    @Operation(summary = "품목별 판매 기록 조회")
    @GetMapping("/product/{productId}")
    public Result<List<SalesRecordDTO>> getSalesRecordsByProduct(
            @PathVariable Long productId) {
        List<SalesRecordDTO> records = salesService.getSalesRecordsByProduct(productId)
                .stream()
                .map(SalesRecordDTO::new)
                .toList();
        return new Result<>(records);
    }
}