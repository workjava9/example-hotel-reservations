package com.example.examplehotelreservations.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;

    private String name;

    private String description;

    private Integer number;

    private Double cost;

    private Integer guestsLimit;

    private List<Timestamp> unavailableDates;

    private String hotelName;
}
