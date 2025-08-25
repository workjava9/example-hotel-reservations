package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.RoomMapper;
import com.example.examplehotelreservations.service.RoomService;
import com.example.examplehotelreservations.web.model.hotel.Room;
import com.example.examplehotelreservations.web.request.RoomRequest;
import com.example.examplehotelreservations.web.response.RoomResponse;
import com.example.examplehotelreservations.web.response.RoomResponseList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerWebTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean RoomService roomService;
    @MockitoBean RoomMapper roomMapper;

    @Test
    @DisplayName("GET /api/v1/room returns rooms")
    void findAll() throws Exception {
        when(roomService.findAll()).thenReturn(java.util.List.of(new Room()));
        when(roomMapper.roomListToRoomResponseList(anyList()))
                .thenReturn(new RoomResponseList(java.util.List.of(
                        new RoomResponse(1L, "r", "d", 101, 100.0, 2, java.util.List.of(), "H"))));

        mvc.perform(get("/api/v1/room"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rooms", hasSize(1)))
                .andExpect(jsonPath("$.rooms[0].id", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/room/{id} returns room")
    void findById() throws Exception {
        when(roomService.findById(4L)).thenReturn(new Room());
        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(new RoomResponse(4L, "r", "d", 102, 200.0, 3, java.util.List.of(), "H2"));

        mvc.perform(get("/api/v1/room/{id}", 4))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)));
    }

    @Test
    @DisplayName("POST /api/v1/room creates -> 201")
    void create() throws Exception {
        RoomRequest req = new RoomRequest("r", "d", 101, 100.0, 2, null, 10L);
        when(roomMapper.requestToRoom(any(RoomRequest.class))).thenReturn(new Room());
        when(roomService.create(any(Room.class), eq(10L))).thenReturn(new Room());
        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(new RoomResponse(11L, "r", "d", 101, 100.0, 2, java.util.List.of(), "H"));

        mvc.perform(post("/api/v1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(11)));
    }

    @Test
    @DisplayName("PUT /api/v1/room/{id} updates -> 200")
    void update() throws Exception {
        RoomRequest req = new RoomRequest("r2", "d2", 103, 300.0, 4, null, 20L);
        when(roomMapper.requestToRoom(any(RoomRequest.class))).thenReturn(new Room());
        when(roomService.update(eq(7L), any(Room.class), eq(20L))).thenReturn(new Room());
        when(roomMapper.roomToResponse(any(Room.class)))
                .thenReturn(new RoomResponse(7L, "r2", "d2", 103, 300.0, 4, java.util.List.of(), "H2"));

        mvc.perform(put("/api/v1/room/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.name", is("r2")));
    }

    @Test
    @DisplayName("GET /api/v1/room/filter -> 200")
    void filter() throws Exception {
        when(roomService.findByFilter(any())).thenReturn(java.util.List.of(new Room()));
        when(roomMapper.roomListToRoomResponseList(anyList()))
                .thenReturn(new RoomResponseList(java.util.List.of(
                        new RoomResponse(2L, "rx", "dx", 201, 120.0, 2, java.util.List.of(), "H"))));

        mvc.perform(get("/api/v1/room/filter").param("minCost", "50").param("maxCost", "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rooms[0].id", is(2)));
    }

    @Test
    @DisplayName("DELETE /api/v1/room/{id} -> 204")
    void deleteById() throws Exception {
        mvc.perform(delete("/api/v1/room/{id}", 6))
                .andExpect(status().isNoContent());
        verify(roomService).deleteById(6L);
    }
}
