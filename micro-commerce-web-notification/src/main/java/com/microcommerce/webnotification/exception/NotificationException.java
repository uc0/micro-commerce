package com.microcommerce.webnotification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationException extends RuntimeException {

    private final NotificationExceptionCode code;

}
