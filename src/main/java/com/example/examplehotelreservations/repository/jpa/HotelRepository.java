package com.example.examplehotelreservations.repository.jpa;

import com.example.examplehotelreservations.web.model.Hotel;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@NonNullApi
@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> , JpaSpecificationExecutor<Hotel> {

    Page<Hotel> findAll(Pageable page);
}
