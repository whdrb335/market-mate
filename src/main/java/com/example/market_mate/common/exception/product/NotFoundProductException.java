package com.example.market_mate.common.exception.product;

import com.example.market_mate.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundProductException extends BaseException {
    public NotFoundProductException(String message, HttpStatus status) {
        super(message, status);
    }
}