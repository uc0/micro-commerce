package com.microcommerce.member.util;

import com.microcommerce.member.exception.MemberException;
import com.microcommerce.member.exception.MemberExceptionCode;
import org.springframework.http.HttpHeaders;

public class HeaderCheckUtil {

    public static void checkUserId(final HttpHeaders headers, final long userId) {
        final long headerUserId;
        try {
            final String headerUserIdStr = headers.getFirst("x-user-id");
            if (headerUserIdStr == null) {
                throw new MemberException(MemberExceptionCode.UNAUTHORIZED);
            }

            headerUserId = Long.parseLong(headerUserIdStr);
        } catch (NumberFormatException e) {
            throw new MemberException(MemberExceptionCode.UNAUTHORIZED);
        }

        if (userId != headerUserId) {
            throw new MemberException(MemberExceptionCode.FORBIDDEN);
        }
    }

}
