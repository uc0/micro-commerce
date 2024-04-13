package com.microcommerce.webnotification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationExceptionCode {

    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED, "UNAUTHORIZED",
            "잘못된 접근 권한"
    ),

    FORBIDDEN(
            HttpStatus.FORBIDDEN, "FORBIDDEN",
            "잘못된 접근 권한"
    ),

    NOTIFICATION_CONNECTION_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION_CONNECTION_ERROR",
            "연결 실패"
    ),

    NOT_FOUND_EMITTER(
            HttpStatus.BAD_REQUEST, "NOT_FOUND_EMITTER",
            "연결 정보 찾을 수 없음"
    );

    private final HttpStatus status;
    private final String code;
    private final String description;

}
