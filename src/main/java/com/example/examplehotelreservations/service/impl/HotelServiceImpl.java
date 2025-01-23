package com.example.examplehotelreservations.service.impl;

import com.example.hotel_cms.exception.BadRequestException;
import com.example.hotel_cms.exception.EntityNotFoundException;
import com.example.hotel_cms.model.Hotel;
import com.example.hotel_cms.repository.jpa.HotelRepository;
import com.example.hotel_cms.repository.jpa.HotelSpecification;
import com.example.hotel_cms.service.HotelService;
import com.example.hotel_cms.service.ManageHotelService;
import com.example.hotel_cms.utility.BeanUtils;
import com.example.hotel_cms.web.filter.HotelFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService, ManageHotelService {

    private final HotelRepository hotelRepository;

    @Override
    public Hotel    create(Hotel hotel) {
        if (hotel.getReviewCount() == (null))
            hotel.setReviewCount(0);
        if (hotel.getRating() == (null))
            hotel.setRating(0.0);

        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel update(Long id,Hotel hotel) {
        Hotel hotelToUpdate = findById(id);
        BeanUtils.copyProperties(hotelToUpdate,hotel);
        return hotelRepository.save(hotelToUpdate);
    }

    @Override
    public Hotel findById(Long id) {
        return hotelRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(
                MessageFormat.format("hotel with id {0} not found", id)));
    }

    @Override
    public List<Hotel> findAll(HotelFilter filter) {
        return hotelRepository.findAll(PageRequest.of(
                filter.getPageNumber(),
                filter.getPageSize()
        )).getContent();
    }

    @Override
    public List<Hotel> findByFilter(HotelFilter filter) {
        return hotelRepository.findAll(
                HotelSpecification.withFilter(filter),
                PageRequest.of(
                        filter.getPageNumber(), filter.getPageSize()
        )).getContent();
    }

    @Override
    public void deleteById(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Hotel updateRating(Long hotelId, Integer newMark) {
        Hotel hotel = findById(hotelId);

        if (newMark > 5 || newMark < 1)
            throw new BadRequestException("mark should be in range 1 to 5");

        int reviewCount = hotel.getReviewCount() != null ? hotel.getReviewCount() : 0;
        double totalRating = (hotel.getRating() != null ? hotel.getRating() : 0) * reviewCount;

        totalRating = totalRating + newMark;
        reviewCount++;

        hotel.setRating(Math.round((totalRating / reviewCount) * 10) / 10.0);
        hotel.setReviewCount(reviewCount);

        return hotelRepository.save(hotel);
    }
}
