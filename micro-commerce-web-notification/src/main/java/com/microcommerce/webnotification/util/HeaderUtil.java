package com.microcommerce.webnotification.util;

import com.microcommerce.webnotification.exception.NotificationException;
import com.microcommerce.webnotification.exception.NotificationExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
public class HeaderUtil {

    public static long getUserId(final HttpHeaders headers) {
        try {
            final String headerUserIdStr = headers.getFirst("x-user-id");
            if (headerUserIdStr == null) {
                throw new NotificationException(NotificationExceptionCode.UNAUTHORIZED);
            }

            return Long.parseLong(headerUserIdStr);
        } catch (final NumberFormatException e) {
            log.error(e.getMessage());
            throw new NotificationException(NotificationExceptionCode.UNAUTHORIZED);
        }
    }

}
