package com.example.examplehotelreservations.service;

import com.example.examplehotelreservations.web.model.Hotel;

public interface ManageHotelService {

    Hotel updateRating(Long hotelId, Integer newMark);
}
