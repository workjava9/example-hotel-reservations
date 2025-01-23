package com.example.examplehotelreservations.service.impl;

import com.example.hotel_cms.exception.BookedRoomException;
import com.example.hotel_cms.exception.EntityNotFoundException;
import com.example.hotel_cms.mapping.BookingMapper;
import com.example.hotel_cms.model.Booking;
import com.example.hotel_cms.model.Room;
import com.example.hotel_cms.model.User;
import com.example.hotel_cms.model.kafka.BookingEvent;
import com.example.hotel_cms.repository.jpa.BookingRepository;
import com.example.hotel_cms.repository.jpa.RoomRepository;
import com.example.hotel_cms.service.BookingService;
import com.example.hotel_cms.service.RoomService;
import com.example.hotel_cms.service.UserService;
import com.example.hotel_cms.web.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    @Value("${app.kafka.kafkaBookingTopic}")
    private String topicName;


    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Override
    @Transactional
    public Booking create(Booking booking, Long roomId, Long userId) {

        Room room = roomService.findById(roomId);
        User user = userService.findById(userId);
        List<Booking> bookings = bookingRepository.findByRoomId(roomId);
        bookings.forEach(b -> {
            if (isOverLap(b.getCheckInDate(),b.getCheckOutDate(),booking.getCheckInDate(),booking.getCheckOutDate()))
                throw new BookedRoomException("Room is not available for the selected dates");
        });
        booking.setRoom(room);
        booking.setUser(user);

        List<Date> newUnavailableDates = getDatesBetween(booking.getCheckInDate(), booking.getCheckOutDate());
        room.getUnavailableDates().addAll(newUnavailableDates);
        roomRepository.save(room);

        Booking savedBooking = bookingRepository.save(booking);

        // Send event to Kafka
        BookingEvent event = new BookingEvent();
        event.setUserId(userId);
        event.setCheckInDate(booking.getCheckInDate());
        event.setCheckOutDate(booking.getCheckOutDate());
        kafkaTemplate.send(topicName, event);

        return savedBooking;
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(bookingMapper::bookingToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(
                MessageFormat.format("booking with id {0} not found", id)));
    }


    private boolean isOverLap(Date start1, Date end1, Date start2, Date end2){
        if ((start2.before(start1) && end2.before(start1)) || (start2.after(end1)&& end2.after(end1)))
            return false;
        else return true;
    }

    private List<Date> getDatesBetween(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

}
