package org.likelion.likelion_12th_team05.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode { // 반복적으로 사용될 Error 상태와 메세지, 코드를 정의
                        // 시스템에 비정상적인 상황이 발생했을 경우
    /**
     * 404 NOT FOUND
     */
    CURATIONS_NOT_FOUND_EXCEPTION("해당 큐레이션이 없습니다. NOT_FOUND_404"),
    LOCATIONS_NOT_FOUND_EXCEPTION("해당 위치가 없습니다. NOT_FOUND_404"),
    USER_NOT_FOUND_EXCEPTION("해당 사용자가 없습니다. NOT_FOUND_404"),
    FILE_NOT_FOUND_EXCEPTION("파일을 찾을 수 없습니다."),
    LIKE_NOT_FOUND_EXCEPTION("좋아요를 누르지 않은 큐레이션입니다."),
    NO_AUTHORIZATION_EXCEPTION("권한이 없습니다."),
    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR("알 수 없는 서버 에러가 발생했습니다. INTERNAL_SERVER_ERROR_500"),

    /**
     * 400 BAD REQUEST
     */
    VALIDATION_ERROR("잘못된 요청입니다. BAD_REQUEST_400");

    private final String message;


}
