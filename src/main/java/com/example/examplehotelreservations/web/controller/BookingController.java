package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.BookingMapper;
import com.example.examplehotelreservations.service.BookingService;
import com.example.examplehotelreservations.web.model.hotel.Booking;
import com.example.examplehotelreservations.web.request.BookingRequest;
import com.example.examplehotelreservations.web.response.BookingResponse;
import com.example.examplehotelreservations.web.response.BookingResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody BookingRequest request){
        Booking booking = bookingService.create(bookingMapper.requestToBooking(request),request.getRoomId(),request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.bookingToResponse(booking));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BookingResponseList> getAll(){

        return ResponseEntity.ok(new BookingResponseList(bookingService.getAllBookings()));
    }


}
