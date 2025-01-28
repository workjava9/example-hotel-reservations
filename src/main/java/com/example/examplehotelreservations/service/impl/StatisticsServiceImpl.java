package com.example.examplehotelreservations.service.impl;

import com.example.examplehotelreservations.repository.mongodb.BookEventRepository;
import com.example.examplehotelreservations.repository.mongodb.UserEventRepository;
import com.example.examplehotelreservations.service.StatisticsService;
import com.example.examplehotelreservations.web.model.event.BookingEvent;
import com.example.examplehotelreservations.web.model.event.UserEvent;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserEventRepository userEventRepository;
    private final BookEventRepository bookEventRepository;

    @Override
    public String exportStatisticsToCsv() {
        String csvFile = "statistics.csv";
        List<BookingEvent> bookingEvents = bookEventRepository.findAll();
        List<UserEvent> userEvents = userEventRepository.findAll();

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            writer.writeNext(new String[]{"Event Type", "User ID", "Check-in Date", "Check-out Date"});

            for (BookingEvent event : bookingEvents) {
                writer.writeNext(new String[]{
                        "Booking",
                        event.getUserId().toString(),
                        event.getCheckInDate() != null ? event.getCheckInDate().toString() : "",
                        event.getCheckOutDate() != null ? event.getCheckOutDate().toString() : ""
                });
            }

            for (UserEvent event : userEvents) {
                writer.writeNext(new String[]{
                        "User Registration",
                        event.getUserId().toString()
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return csvFile;
    }
}