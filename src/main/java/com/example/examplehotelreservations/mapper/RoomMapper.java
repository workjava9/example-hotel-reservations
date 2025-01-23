package com.example.examplehotelreservations.mapper;

import com.example.examplehotelreservations.web.model.Room;
import com.example.examplehotelreservations.web.request.RoomRequest;
import com.example.examplehotelreservations.web.response.RoomResponse;
import com.example.examplehotelreservations.web.response.RoomResponseList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    @Mapping(source = "hotel.name", target = "hotelName")
    RoomResponse roomToResponse(Room room);
    Room requestToRoom(RoomRequest request);

    default RoomResponseList roomListToRoomResponseList(List<Room> rooms) {
        List<RoomResponse> roomResponses =
                rooms.stream().map(this::roomToResponse)
                        .collect(Collectors.toList());

        return new RoomResponseList(roomResponses);
    }
}
