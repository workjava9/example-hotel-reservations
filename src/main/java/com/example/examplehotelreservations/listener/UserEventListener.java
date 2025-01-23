package com.example.examplehotelreservations.listener;

import com.example.examplehotelreservations.repository.mongodb.UserEventRepository;
import com.example.examplehotelreservations.web.model.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    @Value("${app.kafka.kafkaUserTopic}")
    private String topicName;

    private final UserEventRepository userEventRepository;

    @KafkaListener(topics = "${app.kafka.kafkaUserTopic}",
            groupId = "${app.kafka.kafkaGroupId}",
            containerFactory = "userEventConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload UserEvent message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY,required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp){

        userEventRepository.save(message);
        log.info("Received message: {}", message);

    }
}