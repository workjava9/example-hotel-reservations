package com.example.examplehotelreservations.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseList {

    private List<HotelResponse> hotels = new ArrayList<>();
}
