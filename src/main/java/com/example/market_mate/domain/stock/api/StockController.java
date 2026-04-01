package com.example.market_mate.domain.stock.api;

import com.example.market_mate.common.Result;
import com.example.market_mate.domain.stock.dto.StockDTO;
import com.example.market_mate.domain.stock.request.UpdateStockRequest;
import com.example.market_mate.domain.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
@Tag(name = "Stock API", description = "재고 관리 API")
public class StockController {

    private final StockService stockService;

    @Operation(summary = "내 전체 재고 조회")
    @GetMapping
    public Result<List<StockDTO>> getMyStocks(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<StockDTO> stocks = stockService.getMyStocks(userId)
                .stream()
                .map(StockDTO::new)
                .toList();
        return new Result<>(stocks);
    }

    @Operation(summary = "품목별 재고 조회")
    @GetMapping("/{productId}")
    public Result<StockDTO> getStock(@PathVariable Long productId) {
        return new Result<>(new StockDTO(stockService.getStock(productId)));
    }

    @Operation(summary = "재고 추가 (발주)")
    @PatchMapping("/{productId}/add")
    public Result<StockDTO> addStock(
            @PathVariable Long productId,
            @Validated @RequestBody UpdateStockRequest request) {
        return new Result<>(new StockDTO(
                stockService.addStock(productId, request.getQuantity())));
    }

    @Operation(summary = "재고 감소 (직접 조정)")
    @PatchMapping("/{productId}/remove")
    public Result<StockDTO> removeStock(
            @PathVariable Long productId,
            @Validated @RequestBody UpdateStockRequest request) {
        return new Result<>(new StockDTO(
                stockService.removeStock(productId, request.getQuantity())));
    }
}