package com.example.examplehotelreservations.repository.jpa;

import com.example.examplehotelreservations.web.model.Booking;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@NonNullApi
@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByRoomId(Long roomId);

    @EntityGraph(attributePaths = {"room"})
    List<Booking> findAll();
}
