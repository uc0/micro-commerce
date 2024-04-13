package com.microcommerce.webnotification.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler({NotificationException.class})
    public ResponseEntity<String> handlerUserException(final NotificationException ex, final HttpServletRequest req) {
        final NotificationExceptionCode code = ex.getCode();

        log.info("[NotificationException] URI: {}, METHOD: {}, MESSAGE: {}, status: {}",
                req.getRequestURI(), req.getMethod(), code.getDescription(), code.getStatus().toString());

        return ResponseEntity.status(code.getStatus()).body(code.getCode());
    }

}
