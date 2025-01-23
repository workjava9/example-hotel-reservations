package com.example.examplehotelreservations.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseList {

    private List<RoomResponse> rooms;
}
