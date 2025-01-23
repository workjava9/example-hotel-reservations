package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.HotelMapper;
import com.example.examplehotelreservations.service.HotelService;
import com.example.examplehotelreservations.service.ManageHotelService;
import com.example.examplehotelreservations.web.filter.HotelFilter;
import com.example.examplehotelreservations.web.model.Hotel;
import com.example.examplehotelreservations.web.request.HotelRequest;
import com.example.examplehotelreservations.web.request.UpdateRatingRequest;
import com.example.examplehotelreservations.web.response.HotelResponse;
import com.example.examplehotelreservations.web.response.HotelResponseList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotel")
public class HotelController {

    private final HotelService hotelService;
    private final ManageHotelService manageHotelService;
    private final HotelMapper hotelMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelRequest request){
        Hotel newHotel = hotelService.create(hotelMapper.requestToHotel(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.hotelToResponse(newHotel));
    }

    @GetMapping
    public ResponseEntity<HotelResponseList> findAll(HotelFilter filter){
        return ResponseEntity.ok(hotelMapper.hotelListToHotelResponseList(
                hotelService.findAll(filter)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(hotelMapper.hotelToResponse(
                hotelService.findById(id)
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id, @Valid @RequestBody HotelRequest request){
        Hotel updatedHotel = hotelService.update(id,hotelMapper.requestToHotel(request));
        return ResponseEntity.ok(hotelMapper.hotelToResponse(updatedHotel));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-rating")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> updateRating(@RequestBody UpdateRatingRequest updateRatingDTO) {
        return ResponseEntity.ok(hotelMapper.hotelToResponse(manageHotelService.updateRating(updateRatingDTO.getHotelId(), updateRatingDTO.getNewMark())));
    }

    @GetMapping("/filter")
    public ResponseEntity<HotelResponseList> filterBy(HotelFilter filter){
        return ResponseEntity.ok(hotelMapper.hotelListToHotelResponseList(
                hotelService.findByFilter(filter)
        ));
    }

}
