package com.example.market_mate.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.entity.ProductStatus;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserIdAndStatus(Long userId, ProductStatus status);
    boolean existsByUserIdAndName(Long userId, String name);
}