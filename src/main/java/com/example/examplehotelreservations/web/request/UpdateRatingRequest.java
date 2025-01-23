package com.example.examplehotelreservations.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRatingRequest {

    private Long hotelId;

    private Integer newMark;
}