package com.microcommerce.webnotification.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
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

    @DltHandler
    private void handleDlt(ConsumerRecord<String, String> record,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.PARTITION) int partitionId,
                           @Header(KafkaHeaders.OFFSET) Long offset,
                           @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
                           @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.error("[DLT Log] received message='{}' with partitionId='{}', offset='{}', topic='{}', groupId='{}', errorMessage='{}'",
                record.value(), partitionId, offset, topic, groupId, errorMessage);
    }

}
