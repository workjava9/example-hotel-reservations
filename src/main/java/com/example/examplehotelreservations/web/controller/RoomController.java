package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.RoomMapper;
import com.example.examplehotelreservations.service.RoomService;
import com.example.examplehotelreservations.web.filter.RoomFilter;
import com.example.examplehotelreservations.web.model.hotel.Room;
import com.example.examplehotelreservations.web.request.RoomRequest;
import com.example.examplehotelreservations.web.response.RoomResponse;
import com.example.examplehotelreservations.web.response.RoomResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> create(@RequestBody RoomRequest request){
        Room room = roomMapper.requestToRoom(request);
        Room createdRoom = roomService.create(room, request.getHotelId());
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.roomToResponse(createdRoom));
    }

    @GetMapping
    public ResponseEntity<RoomResponseList> findAll(){
        return ResponseEntity.ok(roomMapper.roomListToRoomResponseList(
                roomService.findAll()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(
                roomMapper.roomToResponse(roomService.findById(id))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @RequestBody RoomRequest request){
        Room room = roomMapper.requestToRoom(request);
        Room updatedRoom = roomService.update(id,room,request.getHotelId());
        return ResponseEntity.ok(roomMapper.roomToResponse(updatedRoom));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<RoomResponseList> getByFilter(RoomFilter filter){
        return ResponseEntity.ok(roomMapper.roomListToRoomResponseList(roomService.findByFilter(filter)));
    }

}
