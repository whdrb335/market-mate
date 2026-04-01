package com.example.market_mate.common.exception.user;

import org.springframework.http.HttpStatus;

public class NotMatchPasswd extends RuntimeException {
    private final HttpStatus status;

    public NotMatchPasswd(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}