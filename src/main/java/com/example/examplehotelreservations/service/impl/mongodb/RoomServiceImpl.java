package com.example.examplehotelreservations.service.impl.mongodb;

import com.example.examplehotelreservations.exception.EntityNotFoundException;
import com.example.examplehotelreservations.repository.jpa.RoomRepository;
import com.example.examplehotelreservations.repository.jpa.RoomSpecification;
import com.example.examplehotelreservations.service.HotelService;
import com.example.examplehotelreservations.service.RoomService;
import com.example.examplehotelreservations.utils.BeanUtils;
import com.example.examplehotelreservations.web.filter.RoomFilter;
import com.example.examplehotelreservations.web.model.hotel.Hotel;
import com.example.examplehotelreservations.web.model.hotel.Room;
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
