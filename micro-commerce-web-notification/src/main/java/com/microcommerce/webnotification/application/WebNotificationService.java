package com.microcommerce.webnotification.application;

import com.microcommerce.webnotification.exception.NotificationException;
import com.microcommerce.webnotification.exception.NotificationExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebNotificationService {

    public final static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    private final static Long DEFAULT_TIMEOUT = Long.MAX_VALUE;

    private final static String CONNECTION_ESTABLISHED_MSG = "CONNECTION_ESTABLISHED_MSG";

    public SseEmitter connectNotification(final long userId) {
        final SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));

            try {
                sseEmitter.send(
                        SseEmitter.event()
                                .name(CONNECTION_ESTABLISHED_MSG)
                                .data(CONNECTION_ESTABLISHED_MSG)
                );
        } catch (final IOException exception) {
            throw new NotificationException(NotificationExceptionCode.NOTIFICATION_CONNECTION_ERROR);
        }
        return sseEmitter;
    }

    public void sendNotification(final long userId, final String name, final Object message) {
        final SseEmitter sseEmitter = sseEmitters.get(userId);
        if (sseEmitter == null) {
            return;
        }

        try {
            sseEmitter.send(SseEmitter.event().id("").name(name).data(message));
        } catch (final IOException exception) {
            sseEmitters.remove(userId);
            throw new NotificationException(NotificationExceptionCode.NOTIFICATION_CONNECTION_ERROR);
        }
    }

}
