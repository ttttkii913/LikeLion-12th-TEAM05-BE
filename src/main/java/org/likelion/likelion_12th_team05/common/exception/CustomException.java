package org.likelion.likelion_12th_team05.common.exception;

import lombok.Getter;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode error, String message) {
        super(message);
        this.errorCode = error;
    }

}
