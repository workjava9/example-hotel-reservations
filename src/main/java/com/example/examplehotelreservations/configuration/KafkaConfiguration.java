package com.example.examplehotelreservations.configuration;

import com.example.examplehotelreservations.web.model.event.BookingEvent;
import com.example.examplehotelreservations.web.model.event.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.kafkaGroupId}")
    private String kafkaGroupId;

    @Bean
    public @NotNull ProducerFactory<String, BookingEvent> userEventProducerFactory(ObjectMapper objectMapper){
        return getStringBookingEventProducerFactory(objectMapper);
    }

    @Bean
    public KafkaTemplate<String, UserEvent> kafkaTemplate(ProducerFactory<String,UserEvent> userEventProducerFactory){
        return new KafkaTemplate<>(userEventProducerFactory);
    }

    @Bean
    public @NotNull ConsumerFactory<String, BookingEvent> userEventConsumerFactory(ObjectMapper objectMapper){
        return getStringBookingEventConsumerFactory(objectMapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> userEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, UserEvent> userEventConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<String,UserEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userEventConsumerFactory);

        return factory;
    }

    @Bean
    public ProducerFactory<String, BookingEvent> bookingEventProducerFactory(ObjectMapper objectMapper){
        return getStringBookingEventProducerFactory(objectMapper);
    }

    @NotNull
    private ProducerFactory<String, BookingEvent> getStringBookingEventProducerFactory(ObjectMapper objectMapper) {
        Map<String,Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, BookingEvent> bookingKafkaTemplate(ProducerFactory<String,BookingEvent> bookingEventProducerFactory){
        return new KafkaTemplate<>(bookingEventProducerFactory);
    }

    @Bean
    public ConsumerFactory<String, BookingEvent> bookingEventConsumerFactory(ObjectMapper objectMapper){
        return getStringBookingEventConsumerFactory(objectMapper);
    }

    @NotNull
    private ConsumerFactory<String, BookingEvent> getStringBookingEventConsumerFactory(ObjectMapper objectMapper) {
        Map<String,Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG , StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        config.put(JsonDeserializer.TRUSTED_PACKAGES,"*");

        return new DefaultKafkaConsumerFactory<>(config,new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingEvent> bookingEventConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, BookingEvent> bookingEventConsumerFactory
    ){
        ConcurrentKafkaListenerContainerFactory<String,BookingEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingEventConsumerFactory);

        return factory;
    }
}
