package org.likelion.likelion_12th_team05.common.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.CustomException;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.likelion.likelion_12th_team05.config.ApiResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomExceptionAdvice {

    // 내부 커스텀 에러 처리
    @ExceptionHandler(CustomException.class)
    public ApiResponseTemplate handleCustomException(CustomException exception) {
        log.error("CustomException : {}", exception.getMessage(), exception);
        return ApiResponseTemplate.errorResponse(exception.getErrorCode(), exception.getMessage());
    }

    // 원인 모를 이유의 예외 발생 시
    @ExceptionHandler(Exception.class)
    public ApiResponseTemplate handleServerException(final Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ApiResponseTemplate.errorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 400 BAD REQUEST validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseTemplate<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage(), e); // 수정: 로그 추가

        // 에러 메시지 생성
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        // 응답 생성
        return ApiResponseTemplate.errorResponse(ErrorCode.VALIDATION_ERROR, convertMapToString(errorMap));
    }

    private String convertMapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // 마지막 쉼표와 띄어쓰기를 제거
        }
        return sb.toString();
    }
}