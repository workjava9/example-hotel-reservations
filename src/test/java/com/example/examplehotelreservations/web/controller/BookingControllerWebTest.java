package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.mapper.BookingMapper;
import com.example.examplehotelreservations.service.BookingService;
import com.example.examplehotelreservations.web.model.hotel.Booking;
import com.example.examplehotelreservations.web.request.BookingRequest;
import com.example.examplehotelreservations.web.response.BookingResponse;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerWebTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    BookingService bookingService;

    @MockitoBean
    BookingMapper bookingMapper;

    @Test
    @DisplayName("POST /api/v1/booking creates -> 201")
    void create() throws Exception {
        java.util.Date in = new java.util.Date();
        java.util.Date out = new java.util.Date(in.getTime() + 86_400_000);
        BookingRequest req = new BookingRequest(in, out, 3L, 2L);

        when(bookingMapper.requestToBooking(any(BookingRequest.class))).thenReturn(new Booking());
        when(bookingService.create(any(Booking.class), eq(3L), eq(2L))).thenReturn(new Booking());
        when(bookingMapper.bookingToResponse(any(Booking.class)))
                .thenReturn(new BookingResponse(100L, in, out, null, null));

        mvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(100)));
    }

    @Test
    @DisplayName("GET /api/v1/booking returns list -> 200")
    void getAll() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(java.util.List.of(
                new BookingResponse(1L, new java.util.Date(), new java.util.Date(), null, null)
        ));

        mvc.perform(get("/api/v1/booking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookings", hasSize(1)))
                .andExpect(jsonPath("$.bookings[0].id", is(1)));
    }
}
