package com.example.examplehotelreservations.repository.jpa;

import com.example.examplehotelreservations.web.model.hotel.Booking;
import com.example.examplehotelreservations.web.model.event.BookingEvent;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.tokens.Token;

import java.util.List;

@NonNullApi
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomId(Long roomId);

    @EntityGraph(attributePaths = {"room"})
    List<Booking> findAll();
}
