//package com.example.examplehotelreservations.web.controller;
//
//import com.example.examplehotelreservations.repository.mongodb.UserEventRepository;
//import com.example.examplehotelreservations.web.model.event.UserEvent;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.timeout;
//import static org.mockito.Mockito.verify;
//
//@SpringBootTest(properties = {
//        "spring.autoconfigure.exclude=" +
//                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
//                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
//                "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration," +
//                "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
//                "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",
//
//        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
//        "spring.kafka.consumer.group-id=test-group",
//        "spring.kafka.consumer.auto-offset-reset=earliest",
//        "spring.kafka.consumer.properties.spring.json.trusted.packages=*",
//        "spring.kafka.listener.auto-startup=true",
//
//        "app.kafka.kafkaUserTopic=user-events-test",
//        "app.kafka.kafkaGroupId=test-group"
//})
//@EmbeddedKafka(topics = {"user-events-test"}, partitions = 1)
//@ActiveProfiles("test")
//class KafkaTest {
//
//    @Autowired
//    private KafkaTemplate<String, UserEvent> kafkaTemplate;
//
//    @MockitoBean
//    private UserEventRepository userEventRepository;
//
//    @Value("${app.kafka.kafkaUserTopic}")
//    private String topicName;
//
//    @Test
//    @DisplayName("UserEvent попадает в listener и сохраняется в репозиторий")
//    void whenSendKafkaMessage_thenListenerSavesToRepository() throws Exception {
//        String eventId = UUID.randomUUID().toString();
//        UserEvent event = new UserEvent();
//        event.setId(eventId);
//        event.setUserId(1L);
//
//        kafkaTemplate.send(topicName, UUID.randomUUID().toString(), event).get(10, TimeUnit.SECONDS);
//        kafkaTemplate.flush();
//
//        verify(userEventRepository, timeout(30_000)).save(
//                argThat(e -> eventId.equals(e.getId()) && Long.valueOf(1L).equals(e.getUserId()))
//        );
//    }
//
//    @TestConfiguration
//    static class KafkaTestOverrides {
//
//        @Bean
//        @Primary
//        ConsumerFactory<String, UserEvent> testUserEventConsumerFactory(
//                @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
//
//            Map<String, Object> props = new HashMap<>();
//            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
//            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//
//            JsonDeserializer<UserEvent> value = new JsonDeserializer<>(UserEvent.class, false);
//            value.addTrustedPackages("*");
//
//            return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), value);
//        }
//
//        @Bean
//        @Primary
//        ProducerFactory<String, UserEvent> testUserEventProducerFactory(
//                @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
//
//            Map<String, Object> props = new HashMap<>();
//            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//
//            return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>());
//        }
//    }
//}
