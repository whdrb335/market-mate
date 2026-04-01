package com.example.market_mate.domain.product.api;

import com.example.market_mate.common.Result;
import com.example.market_mate.domain.product.dto.ProductDTO;
import com.example.market_mate.domain.product.entity.Product;
import com.example.market_mate.domain.product.request.CreateProductRequest;
import com.example.market_mate.domain.product.request.UpdateProductRequest;
import com.example.market_mate.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Tag(name = "Product API", description = "품목 관리 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "품목 등록")
    @PostMapping
    public Result<ProductDTO> createProduct(
            @Validated @RequestBody CreateProductRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        Product product = productService.createProduct(
                userId, request.getName(), request.getUnit(),
                request.getPurchasePrice(), request.getSellingPrice()
        );
        return new Result<>(new ProductDTO(product));
    }

    @Operation(summary = "내 품목 전체 조회")
    @GetMapping
    public Result<List<ProductDTO>> getMyProducts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ProductDTO> products = productService.getMyProducts(userId)
                .stream()
                .map(ProductDTO::new)
                .toList();
        return new Result<>(products);
    }

    @Operation(summary = "품목 수정")
    @PatchMapping("/{productId}")
    public Result<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Validated @RequestBody UpdateProductRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        Product product = productService.updateProduct(
                userId, productId, request.getName(), request.getUnit(),
                request.getPurchasePrice(), request.getSellingPrice()
        );
        return new Result<>(new ProductDTO(product));
    }

    @Operation(summary = "품목 삭제")
    @DeleteMapping("/{productId}")
    public Result<String> deleteProduct(
            @PathVariable Long productId,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        productService.deleteProduct(userId, productId);
        return new Result<>("품목이 삭제되었습니다.");
    }
}