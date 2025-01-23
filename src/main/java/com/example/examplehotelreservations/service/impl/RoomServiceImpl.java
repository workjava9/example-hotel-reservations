package com.example.examplehotelreservations.service.impl;

import com.example.hotel_cms.exception.EntityNotFoundException;
import com.example.hotel_cms.model.Hotel;
import com.example.hotel_cms.model.Room;
import com.example.hotel_cms.repository.jpa.RoomRepository;
import com.example.hotel_cms.repository.jpa.RoomSpecification;
import com.example.hotel_cms.service.HotelService;
import com.example.hotel_cms.service.RoomService;
import com.example.hotel_cms.utility.BeanUtils;
import com.example.hotel_cms.web.filter.RoomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    @Override
    public Room create(Room room , Long hotelId) {
        Hotel hotel = hotelService.findById(hotelId);
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    @Override
    public Room update(Long id, Room room, Long hotelId) {
        Room roomToUpdate = findById(id);
        Hotel hotel = hotelService.findById(hotelId);
        BeanUtils.copyProperties(roomToUpdate, room);
        roomToUpdate.setHotel(hotel);
        return roomRepository.save(roomToUpdate);
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(
                MessageFormat.format("room with id {0} not found", id)));
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> findByFilter(RoomFilter filter) {
        return roomRepository.findAll(RoomSpecification.withFilter(filter),
                PageRequest.of(
                        filter.getPageNumber(), filter.getPageSize()
                )).getContent();
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
}
