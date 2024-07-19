package org.likelion.likelion_12th_team05.common.exception;

import org.likelion.likelion_12th_team05.common.error.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
