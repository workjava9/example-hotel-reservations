package com.example.examplehotelreservations.mapper;

import com.example.examplehotelreservations.web.model.hotel.Hotel;
import com.example.examplehotelreservations.web.request.HotelRequest;
import com.example.examplehotelreservations.web.response.HotelResponse;
import com.example.examplehotelreservations.web.response.HotelResponseList;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    Hotel requestToHotel(HotelRequest request);

    HotelResponse hotelToResponse(Hotel hotel);

    default HotelResponseList hotelListToHotelResponseList(List<Hotel> hotels){
        List<HotelResponse> hotelResponses =
                hotels.stream().map(this::hotelToResponse).collect(Collectors.toList());

        return new HotelResponseList(hotelResponses);
    }

}
