package com.example.examplehotelreservations.service;

import com.example.examplehotelreservations.web.filter.HotelFilter;
import com.example.examplehotelreservations.web.model.hotel.Hotel;
import java.util.List;

public interface HotelService {

    Hotel create(Hotel hotel);

    Hotel update(Long id, Hotel hotel);

    Hotel findById(Long id);

    List<Hotel> findAll(HotelFilter filter);

    List<Hotel> findByFilter(HotelFilter filter);

    void deleteById(Long id);
}
