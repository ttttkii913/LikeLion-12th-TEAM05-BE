package org.likelion.likelion_12th_team05.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.error.SuccessCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseTemplate<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private String message;
    private T data;

    public static <T> ApiResponseTemplate<T> successResponse(T data, SuccessCode successCode) {
        return new ApiResponseTemplate<>(SUCCESS_STATUS, successCode.getMessage(), data);
    }

    public static <T> ApiResponseTemplate<T> successWithNoContent(SuccessCode successCode) {
        return new ApiResponseTemplate<>(SUCCESS_STATUS, successCode.getMessage(), null);
    }

    public static <T> ApiResponseTemplate<T> errorResponse(ErrorCode errorCode) {
        return new ApiResponseTemplate<>(ERROR_STATUS, errorCode.getMessage(), null);
    }

    private ApiResponseTemplate(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
