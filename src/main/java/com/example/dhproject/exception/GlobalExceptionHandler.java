package com.example.dhproject.exception;

import com.example.dhproject.dto.response.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ApiResponseDto> handleError(ErrorException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ApiResponseDto(false, e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDto(false, "예상치 못한 오류가 발생했습니다.", null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto> handleGeneralException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDto(false, "예상치 못한 오류가 발생했습니다.", null));
    }

}
