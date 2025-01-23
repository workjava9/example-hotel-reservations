package com.example.examplehotelreservations.web.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFilter {

    private Integer pageNumber;
    private Integer pageSize;
    private Long id;
    private String name;
    private String headline;
    private String city;
    private String address;
    private Double centerDistance;
    private Double rating;
    private Integer reviewCount;
}
