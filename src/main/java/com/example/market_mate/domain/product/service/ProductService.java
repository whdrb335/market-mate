package com.example.market_mate.domain.product.service;

import com.example.market_mate.common.exception.product.DuplicateProductException;
import com.example.market_mate.common.exception.product.NotFoundProductException;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.entity.ProductStatus;
import com.example.market_mate.domain.product.repository.ProductRepository;
import com.example.market_mate.domain.stock.entity.Stock;
import com.example.market_mate.domain.stock.repository.StockRepository;
import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.repository.UserRepository;
import com.example.market_mate.common.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    /**
     * 품목 등록
     */
    @Transactional
    public Product createProduct(Long userId, String name, String unit,
                                  int purchasePrice, int sellingPrice) {
        User user = getUserById(userId);
        validateDuplicateProduct(userId, name);

        Product product = Product.createProduct(
                user, name, unit, purchasePrice, sellingPrice);
        productRepository.save(product);

        // 품목 등록 시 재고 0으로 자동 생성
        Stock stock = Stock.createStock(product, 0);
        stockRepository.save(stock);

        return product;
    }

    /**
     * 내 품목 전체 조회
     */
    public List<Product> getMyProducts(Long userId) {
        return productRepository.findByUserIdAndStatus(userId, ProductStatus.ACTIVE);
    }

    /**
     * 품목 수정
     */
    @Transactional
    public Product updateProduct(Long userId, Long productId, String name,
                                  String unit, int purchasePrice, int sellingPrice) {
        Product product = getProductById(productId);
        validateProductOwner(userId, product);
        product.updateProduct(name, unit, purchasePrice, sellingPrice);
        return product;
    }

    /**
     * 품목 삭제 (Soft Delete)
     */
    @Transactional
    public void deleteProduct(Long userId, Long productId) {
        Product product = getProductById(productId);
        validateProductOwner(userId, product);
        product.delete();
    }

    // == 공통 메서드 ==
    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException(
                        "존재하지 않는 품목입니다.", HttpStatus.NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(
                        "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
    }

    private void validateDuplicateProduct(Long userId, String name) {
        if (productRepository.existsByUserIdAndName(userId, name)) {
            throw new DuplicateProductException(
                    "이미 존재하는 품목입니다.", HttpStatus.CONFLICT);
        }
    }

    private void validateProductOwner(Long userId, Product product) {
        if (!product.getUser().getId().equals(userId)) {
            throw new NotFoundProductException(
                    "본인 품목만 수정/삭제할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
    }
}