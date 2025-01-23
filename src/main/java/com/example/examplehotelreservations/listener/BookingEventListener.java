package com.example.examplehotelreservations.listener;

import com.example.examplehotelreservations.repository.mongodb.BookEventRepository;
import com.example.examplehotelreservations.web.model.event.BookingEvent;
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
public class BookingEventListener {

    @Value("${app.kafka.kafkaBookingTopic}")
    private String topicName;

    private final BookEventRepository bookingEventRepository;

    @KafkaListener(topics = "${app.kafka.kafkaBookingTopic}",
            groupId = "${app.kafka.kafkaGroupId}",
            containerFactory = "bookingEventConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload BookingEvent message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY,required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp){

        bookingEventRepository.save(message);
        log.info("Received message: {}", message);

    }
}
