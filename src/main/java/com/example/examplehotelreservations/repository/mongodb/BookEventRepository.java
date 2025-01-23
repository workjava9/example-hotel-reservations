package com.example.examplehotelreservations.repository.mongodb;

import com.example.examplehotelreservations.web.model.event.BookingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookEventRepository extends MongoRepository<BookingEvent,String> {
}
