package com.example.market_mate.common.exception.stock;

import com.example.market_mate.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotEnoughStockException extends BaseException {
    public NotEnoughStockException(String message, HttpStatus status) {
        super(message, status);
    }
}