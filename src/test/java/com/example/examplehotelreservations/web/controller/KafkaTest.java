package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.repository.mongodb.UserEventRepository;
import com.example.examplehotelreservations.web.model.event.UserEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.awaitility.Awaitility;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class KafkaTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private UserEventRepository userEventRepository;

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("""
                    confluentinc/cp-kafka:7.3.3""")
    );

    @DynamicPropertySource
    static void registryKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Value("${app.kafka.kafkaUserTopic}")
    private String topicName;

    @Test
    public void whenSendKafkaMessage_ThenHandleMessageByListener() {
        String eventId = UUID.randomUUID().toString();
        UserEvent event = new UserEvent();
        event.setId(eventId);
        event.setUserId(1L);

        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(topicName, key, event);


        Awaitility.await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(60, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<UserEvent> mayBeUserEvent = userEventRepository.findById(eventId);


                    assertThat(mayBeUserEvent).isPresent();

                    UserEvent receivedEvent = mayBeUserEvent.get();
                    assertThat(receivedEvent.getUserId()).isEqualTo(1L);
                });
    }
}