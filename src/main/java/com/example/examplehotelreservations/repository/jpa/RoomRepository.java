package com.example.examplehotelreservations.repository.jpa;

import com.example.examplehotelreservations.web.model.hotel.Room;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@NonNullApi
@Repository
public interface RoomRepository extends JpaRepository<Room,Long> , JpaSpecificationExecutor<Room> {

    @EntityGraph(attributePaths = {"hotel","unavailableDates"})
    List<Room> findAll();

    @EntityGraph(attributePaths = {"hotel","unavailableDates"})
    Optional<Room> findById(Long id);

    @EntityGraph(attributePaths = {"hotel","unavailableDates"})
    Page<Room> findAll(Pageable page);

}
