package com.example.examplehotelreservations.web.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomFilter {

    private Long id;

    private String name;

    private Double minCost;

    private Double maxCost;

    private Integer guestsLimit;

    private Long hotelId;

    private Integer pageNumber;

    private Integer pageSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkOutDate;

}


