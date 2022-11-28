package com.example.hospitalreview.exception;

import com.example.hospitalreview.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 특정 위치의 컨트롤러로 갈수 있게 해줌
// Client에서도 에러메세지를 확인할 수 있는 방법
@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(HospitalReviewAppException.class)
    // <?> 어떤타입이라도 들어올 수 있음
    public ResponseEntity<?> hospitalReviewAppExceptionHandler(HospitalReviewAppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(e.getMessage()));
    }
}
