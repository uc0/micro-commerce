package com.microcommerce.webnotification.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private static final short REPLICATION_FACTOR = 1;

    private static final int REPLICATION_NUM_PARTITION = 5;

    private static final int RETRY_COUNT = 3;

    private static final int BACK_OFF_PERIOD = 2000;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "web-notification-consumer-group-" + UUID.randomUUID());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public RetryTopicConfiguration retryTopicConfig(KafkaTemplate<String, String> kafkaTemplate) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .autoCreateTopicsWith(REPLICATION_NUM_PARTITION, REPLICATION_FACTOR)
                .maxAttempts(RETRY_COUNT)
                .retryOn(Exception.class)
                .fixedBackOff(BACK_OFF_PERIOD)
                .listenerFactory(kafkaListenerContainerFactory())
                .setTopicSuffixingStrategy(TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
                .create(kafkaTemplate);
    }


}
