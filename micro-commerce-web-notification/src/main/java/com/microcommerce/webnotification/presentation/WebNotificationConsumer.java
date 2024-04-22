package com.microcommerce.webnotification.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcommerce.webnotification.application.WebNotificationService;
import com.microcommerce.webnotification.domain.constant.KafkaTopic;
import com.microcommerce.webnotification.domain.message.NotificationMessage;
import com.microcommerce.webnotification.exception.NotificationException;
import com.microcommerce.webnotification.infrastructure.kafka.KafkaProducer;
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

    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = KafkaTopic.SEND_NOTIFICATION)
    public void order(final ConsumerRecord<String, String> record, final Acknowledgment acknowledgment) {
        NotificationMessage msg = null;
        try {
            msg = objectMapper.readValue(record.value(), NotificationMessage.class);
            // TODO: consumer retry test code
//            if (msg != null) {
//                throw new RuntimeException("test exception");
//            }

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
            sendRetryMessage(msg);
        }
        finally {
            acknowledgment.acknowledge();
        }
    }

    private void sendRetryMessage(NotificationMessage msg) {
        if (msg != null) {
            if (msg.retryCount() >= 3) {
                log.error("[재시도 실패] TODO: 실패 정보 DB에 저장");
                return;
            }

            kafkaProducer.send(
                    KafkaTopic.SEND_NOTIFICATION,
                    new NotificationMessage(msg.userId(), msg.messageName(), msg.messageContent(), true, msg.retryCount() + 1)
            );
        } else {
            log.error("[메시지 읽기 실패] TODO: 실패 정보 DB에 저장");
        }
    }

}
