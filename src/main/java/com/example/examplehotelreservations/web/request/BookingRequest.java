package com.example.examplehotelreservations.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkOutDate;

    private Long roomId;

    private Long userId;

}
