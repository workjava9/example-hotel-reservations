package com.example.examplehotelreservations.service;

import com.example.examplehotelreservations.web.model.hotel.Booking;
import com.example.examplehotelreservations.web.response.BookingResponse;
import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    Booking findById(Long id);

    Booking create(Booking booking, Long roomId, Long userId);

    List<BookingResponse> getAllBookings();
}
