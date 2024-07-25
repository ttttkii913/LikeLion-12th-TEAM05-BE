package org.likelion.likelion_12th_team05.common.advice;

import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionAdvice {

    // 엔티티 not found 에러 처리 핸들러
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleNotFoundException(NotFoundException ex) {
        ApiResponseTemplate<Void> errorResponse = ApiResponseTemplate.errorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}