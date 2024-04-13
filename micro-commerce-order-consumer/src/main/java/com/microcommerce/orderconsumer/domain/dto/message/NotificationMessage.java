package com.microcommerce.orderconsumer.domain.dto.message;

public record NotificationMessage(long userId,
                                  String messageName,
                                  String messageContent,
                                  boolean isWeb,
                                  boolean isSms,
                                  boolean isEmail) {
}
