package com.example.examplehotelreservations.service;

import com.example.examplehotelreservations.web.filter.RoomFilter;
import com.example.examplehotelreservations.web.model.Room;
import java.util.List;

public interface RoomService {

    Room create(Room room, Long hotelId);

    Room update(Long id, Room room, Long hotelId);

    Room findById(Long id);

    List<Room> findAll();

    List<Room> findByFilter(RoomFilter filter);

    void deleteById(Long id);
}
