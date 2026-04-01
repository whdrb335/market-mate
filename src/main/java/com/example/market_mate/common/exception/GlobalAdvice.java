package com.example.market_mate.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalAdvice {

    // 커스텀 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResult> handleBaseException(BaseException e) {
        log.error("[BaseException] = {}", e.getMessage());
        return new ResponseEntity<>(
            new ErrorResult(e.getStatus().name(), e.getMessage()),
            e.getStatus()
        );
    }

    // @Validated 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleValidationException(
            MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("검증 실패");

        log.error("[ValidationException] = {}", errorMessage);
        return new ResponseEntity<>(
            new ErrorResult("VALIDATION_ERROR", errorMessage),
            org.springframework.http.HttpStatus.BAD_REQUEST
        );
    }

    // 그 외 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleException(Exception e) {
        log.error("[Exception] = {}", e.getMessage());
        return new ResponseEntity<>(
            new ErrorResult("SERVER_ERROR", "서버 오류가 발생했습니다"),
            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}