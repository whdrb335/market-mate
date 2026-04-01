package com.example.market_mate.domain.stock.service;

import com.example.market_mate.common.exception.product.NotFoundProductException;
import com.example.market_mate.common.exception.stock.NotEnoughStockException;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.entity.ProductStatus;
import com.example.market_mate.domain.product.repository.ProductRepository;
import com.example.market_mate.domain.stock.entity.Stock;
import com.example.market_mate.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    /**
     * 재고 조회 (품목별)
     */
    public Stock getStock(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundProductException(
                        "재고 정보가 없습니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 내 전체 재고 조회
     */
    public List<Stock> getMyStocks(Long userId) {
        List<Product> products = productRepository
                .findByUserIdAndStatus(userId, ProductStatus.ACTIVE);
        return products.stream()
                .map(product -> stockRepository.findByProductId(product.getId())
                        .orElseThrow(() -> new NotFoundProductException(
                                "재고 정보가 없습니다.", HttpStatus.NOT_FOUND)))
                .toList();
    }

    /**
     * 재고 추가 (발주)
     */
    @Transactional
    public Stock addStock(Long productId, int quantity) {
        Stock stock = getStock(productId);
        stock.addStock(quantity);
        return stock;
    }

    /**
     * 재고 감소 (직접 조정)
     */
    @Transactional
    public Stock removeStock(Long productId, int quantity) {
        Stock stock = getStock(productId);
        stock.removeStock(quantity);
        return stock;
    }
}