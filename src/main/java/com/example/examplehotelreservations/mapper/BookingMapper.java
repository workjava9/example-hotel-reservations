package com.example.examplehotelreservations.mapper;

import com.example.examplehotelreservations.web.model.hotel.Booking;
import com.example.examplehotelreservations.web.request.BookingRequest;
import com.example.examplehotelreservations.web.response.BookingResponse;
import com.example.examplehotelreservations.web.response.BookingResponseList;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, RoomMapper.class})
public interface BookingMapper {

    Booking requestToBooking(BookingRequest request);

    BookingResponse bookingToResponse(Booking booking);

    default BookingResponseList bookingListToBookingResponseList(List<Booking> bookings) {
        List<BookingResponse> bookingResponses =
                bookings.stream().map(this::bookingToResponse).collect(Collectors.toList());

        return new BookingResponseList(bookingResponses);
    }


}
