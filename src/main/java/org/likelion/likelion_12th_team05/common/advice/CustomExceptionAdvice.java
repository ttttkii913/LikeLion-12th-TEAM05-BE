package org.likelion.likelion_12th_team05.common.advice;

import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionAdvice.class);

    // 엔티티 not found 에러 처리 핸들러
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleNotFoundException(NotFoundException ex) {
        ApiResponseTemplate<Void> errorResponse = ApiResponseTemplate.errorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 내부 커스텀 에러 처리
    @ExceptionHandler(CustomException.class)
    public ApiResponseTemplate handleCustomException(CustomException exception) {
        log.error("CustomException : {}", exception.getMessage(), exception);
        return ApiResponseTemplate.errorResponse(exception.getErrorCode(), exception.getMessage());
    }

    // 원인 모를 이유의 예외 발생 시
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseTemplate handleServerException(final Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ApiResponseTemplate.errorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}