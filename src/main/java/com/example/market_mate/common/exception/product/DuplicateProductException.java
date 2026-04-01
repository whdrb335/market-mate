package com.example.market_mate.common.exception.product;

import com.example.market_mate.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateProductException extends BaseException {
    public DuplicateProductException(String message, HttpStatus status) {
        super(message, status);
    }
}