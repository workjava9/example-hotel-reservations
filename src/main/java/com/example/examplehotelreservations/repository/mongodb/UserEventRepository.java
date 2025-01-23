package com.example.examplehotelreservations.repository.mongodb;

import com.example.examplehotelreservations.web.model.event.UserEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends MongoRepository<UserEvent,String> {
}
