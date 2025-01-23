package com.example.examplehotelreservations.web.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookingEvents")
public class BookingEvent {

    @Id
    private String id;

    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkOutDate;

}
