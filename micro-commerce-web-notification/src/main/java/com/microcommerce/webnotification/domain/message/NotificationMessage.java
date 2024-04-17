package com.microcommerce.webnotification.domain.message;

public record NotificationMessage(long userId,
                                  String messageName,
                                  String messageContent,
                                  boolean isWeb) {
}
