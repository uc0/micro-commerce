package com.microcommerce.webnotification.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcommerce.webnotification.application.WebNotificationService;
import com.microcommerce.webnotification.domain.constant.KafkaTopic;
import com.microcommerce.webnotification.domain.message.NotificationMessage;
import com.microcommerce.webnotification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebNotificationConsumer {

    private final ObjectMapper objectMapper;

    private final WebNotificationService webNotificationService;

    @KafkaListener(topics = KafkaTopic.SEND_NOTIFICATION)
    public void order(final ConsumerRecord<String, String> record, final Acknowledgment acknowledgment) {
        NotificationMessage msg;
        try {
            msg = objectMapper.readValue(record.value(), NotificationMessage.class);

            log.info("consume msg: {}", msg.toString());
            log.info("record info: {}", record.offset());

            if (msg.isWeb()) {
                webNotificationService.sendNotification(msg.userId(), msg.messageName(), msg.messageContent());
            }
        } catch (final JsonProcessingException e) {
            log.error("recode parsing error: {}", e.getMessage());
        } catch (final NotificationException e) {
            log.error("web notification error: {}", e.getMessage());
        } catch (final Exception e) {
            log.error("unknown error: {}", e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }
    }

}
