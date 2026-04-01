package com.example.market_mate.common.exception.user;

import com.example.market_mate.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundUserException extends BaseException {
    public NotFoundUserException(String message, HttpStatus status) {
        super(message, status);
    }
}