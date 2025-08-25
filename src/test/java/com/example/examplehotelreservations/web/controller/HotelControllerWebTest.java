package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.HotelMapper;
import com.example.examplehotelreservations.service.HotelService;
import com.example.examplehotelreservations.service.ManageHotelService;
import com.example.examplehotelreservations.web.model.hotel.Hotel;
import com.example.examplehotelreservations.web.request.HotelRequest;
import com.example.examplehotelreservations.web.request.UpdateRatingRequest;
import com.example.examplehotelreservations.web.response.HotelResponse;
import com.example.examplehotelreservations.web.response.HotelResponseList;
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

@WebMvcTest(controllers = HotelController.class)
@AutoConfigureMockMvc(addFilters = false)
class HotelControllerWebTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean HotelService hotelService;
    @MockitoBean ManageHotelService manageHotelService;
    @MockitoBean HotelMapper hotelMapper;

    @Test
    @DisplayName("GET /api/v1/hotel returns hotels")
    void findAll() throws Exception {
        when(hotelService.findAll(any())).thenReturn(java.util.List.of(new Hotel()));
        when(hotelMapper.hotelListToHotelResponseList(anyList()))
                .thenReturn(new HotelResponseList(java.util.List.of(
                        new HotelResponse(1L, "h", "x", "c", "addr", 1.0, 4.5, 10))));

        mvc.perform(get("/api/v1/hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotels", hasSize(1)))
                .andExpect(jsonPath("$.hotels[0].id", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/hotel/{id} returns hotel")
    void findById() throws Exception {
        when(hotelService.findById(3L)).thenReturn(new Hotel());
        when(hotelMapper.hotelToResponse(any(Hotel.class)))
                .thenReturn(new HotelResponse(3L, "h", "x", "c", "addr", 2.0, 4.2, 8));

        mvc.perform(get("/api/v1/hotel/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    @DisplayName("POST /api/v1/hotel creates -> 201")
    void create() throws Exception {
        HotelRequest req = new HotelRequest("h", "hh", "c", "addr", 1.0);
        when(hotelMapper.requestToHotel(any(HotelRequest.class))).thenReturn(new Hotel());
        when(hotelService.create(any(Hotel.class))).thenReturn(new Hotel());
        when(hotelMapper.hotelToResponse(any(Hotel.class)))
                .thenReturn(new HotelResponse(11L, "h", "hh", "c", "addr", 1.0, 5.0, 0));

        mvc.perform(post("/api/v1/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(11)));
    }

    @Test
    @DisplayName("PUT /api/v1/hotel/{id} updates -> 200")
    void update() throws Exception {
        HotelRequest req = new HotelRequest("h2", "hh2", "c2", "addr2", 2.0);
        when(hotelMapper.requestToHotel(any(HotelRequest.class))).thenReturn(new Hotel());
        when(hotelService.update(eq(9L), any(Hotel.class))).thenReturn(new Hotel());
        when(hotelMapper.hotelToResponse(any(Hotel.class)))
                .thenReturn(new HotelResponse(9L, "h2", "hh2", "c2", "addr2", 2.0, 4.7, 5));

        mvc.perform(put("/api/v1/hotel/{id}", 9)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.name", is("h2")));
    }

    @Test
    @DisplayName("PUT /api/v1/hotel/update-rating -> 200")
    void updateRating() throws Exception {
        UpdateRatingRequest req = new UpdateRatingRequest(5L, 5);
        when(manageHotelService.updateRating(5L, 5)).thenReturn(new Hotel());
        when(hotelMapper.hotelToResponse(any(Hotel.class)))
                .thenReturn(new HotelResponse(5L, "h", "hh", "c", "addr", 1.0, 5.0, 10));

        mvc.perform(put("/api/v1/hotel/update-rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating", is(5.0)));
    }

    @Test
    @DisplayName("GET /api/v1/hotel/filter -> 200")
    void filter() throws Exception {
        when(hotelService.findByFilter(any())).thenReturn(java.util.List.of(new Hotel()));
        when(hotelMapper.hotelListToHotelResponseList(anyList()))
                .thenReturn(new HotelResponseList(java.util.List.of(
                        new HotelResponse(2L, "hx", "x", "c", "a", 3.0, 4.0, 1))));

        mvc.perform(get("/api/v1/hotel/filter").param("city", "c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotels[0].id", is(2)));
    }

    @Test
    @DisplayName("DELETE /api/v1/hotel/{id} -> 204")
    void deleteById() throws Exception {
        mvc.perform(delete("/api/v1/hotel/{id}", 6))
                .andExpect(status().isNoContent());
        verify(hotelService).deleteById(6L);
    }
}
