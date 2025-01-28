package com.example.examplehotelreservations.web.controller;

import com.example.examplehotelreservations.AbstractTestController;
import com.example.examplehotelreservations.exception.EntityNotFoundException;
import com.example.examplehotelreservations.mapper.HotelMapper;
import com.example.examplehotelreservations.service.HotelService;
import com.example.examplehotelreservations.service.ManageHotelService;
import com.example.examplehotelreservations.web.StringTestUtils;
import com.example.examplehotelreservations.web.filter.HotelFilter;
import com.example.examplehotelreservations.web.model.hotel.Hotel;
import com.example.examplehotelreservations.web.request.HotelRequest;
import com.example.examplehotelreservations.web.response.HotelResponse;
import com.example.examplehotelreservations.web.response.HotelResponseList;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HotelControllerTest extends AbstractTestController {

    @MockitoBean
    private HotelService hotelService;

    @MockitoBean
    private HotelMapper hotelMapper;

    @MockitoBean
    private ManageHotelService manageHotelService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenFindAll_thenReturnAllHotels() throws Exception {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(createHotel(1L));
        hotels.add(createHotel(2L));

        List<HotelResponse> hotelResponses = new ArrayList<>();
        hotelResponses.add(createHotelResponse(1L));
        hotelResponses.add(createHotelResponse(2L));

        HotelResponseList hotelResponseList = new HotelResponseList(hotelResponses);

        HotelFilter hotelFilter = new HotelFilter();
        hotelFilter.setPageSize(10);
        hotelFilter.setPageNumber(0);

        Mockito.when(hotelService.findAll(hotelFilter)).thenReturn(hotels);
        Mockito.when(hotelMapper.hotelListToHotelResponseList(hotels)).thenReturn(hotelResponseList);

        String actualResponse = mockMvc.perform(get("/api/v1/hotel")
                .param("pageSize", "10").param("pageNumber", "0"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_hotels_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).findAll(hotelFilter);
        Mockito.verify(hotelMapper, Mockito.times(1)).hotelListToHotelResponseList(hotels);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "user")
    public void whenFindById_thenReturnHotel() throws Exception {
        Hotel hotel = createHotel(1L);
        HotelResponse hotelResponse = createHotelResponse(1L);

        Mockito.when(hotelService.findById(1L)).thenReturn(hotel);
        Mockito.when(hotelMapper.hotelToResponse(hotel)).thenReturn(hotelResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/hotel/1"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_hotel_by_id_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).findById(1L);
        Mockito.verify(hotelMapper, Mockito.times(1)).hotelToResponse(hotel);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenCreateHotel_thenReturnNewHotel() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("hotel 1");
        hotel.setHeadline("hotel");
        hotel.setCity("city");
        hotel.setAddress("address");
        hotel.setCenterDistance(0.0);
        hotel.setRating(0.0);
        hotel.setReviewCount(0);

        Hotel createdHotel = createHotel(1L);
        HotelResponse hotelResponse = createHotelResponse(1L);
        HotelRequest hotelRequest = new HotelRequest();

        hotelRequest.setName("hotel 1");
        hotelRequest.setHeadline("hotel");
        hotelRequest.setCity("city");
        hotelRequest.setAddress("address");
        hotelRequest.setCenterDistance(1.0);

        Mockito.when(hotelService.create(hotel)).thenReturn(createdHotel);
        Mockito.when(hotelMapper.requestToHotel(hotelRequest)).thenReturn(hotel);
        Mockito.when(hotelMapper.hotelToResponse(createdHotel)).thenReturn(hotelResponse);

        String actualResponse = mockMvc.perform(post("/api/v1/hotel").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hotelRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/create_hotel_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).create(hotel);
        Mockito.verify(hotelMapper, Mockito.times(1)).requestToHotel(hotelRequest);
        Mockito.verify(hotelMapper, Mockito.times(1)).hotelToResponse(createdHotel);


        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenUpdateHotelThenReturnUpdatedHotel() throws Exception {
        HotelRequest request = new HotelRequest();

        request.setName("new hotel 1");
        request.setHeadline("hotel");
        request.setCity("city");
        request.setAddress("address");
        request.setCenterDistance(1.0);

        Hotel updatedHotel = new Hotel();
        updatedHotel.setId(1L);
        updatedHotel.setName("new hotel 1");
        updatedHotel.setHeadline("hotel");
        updatedHotel.setCity("city");
        updatedHotel.setAddress("address");
        updatedHotel.setCenterDistance(0.0);
        updatedHotel.setRating(0.0);
        updatedHotel.setReviewCount(0);

        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setId(1L);
        hotelResponse.setName("new hotel 1");
        hotelResponse.setHeadline("hotel");
        hotelResponse.setCity("city");
        hotelResponse.setAddress("address");
        hotelResponse.setCenterDistance(0.0);
        hotelResponse.setRating(0.0);
        hotelResponse.setReviewCount(0);

        Mockito.when(hotelService.update(1L, updatedHotel)).thenReturn(updatedHotel);
        Mockito.when(hotelMapper.requestToHotel(request)).thenReturn(updatedHotel);
        Mockito.when(hotelMapper.hotelToResponse(updatedHotel)).thenReturn(hotelResponse);


        String actualResponse = mockMvc.perform(put("/api/v1/hotel/1")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Mockito.verify(hotelService, Mockito.times(1)).update(1L, updatedHotel);
        Mockito.verify(hotelMapper, Mockito.times(1)).requestToHotel(request);
        Mockito.verify(hotelMapper, Mockito.times(1)).hotelToResponse(updatedHotel);

        String expectedResponse = StringTestUtils.readStringFromResource("response/update_hotel_response.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenDeleteHotelById_ThenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/hotel/1")).andExpect(status().isNoContent());

        Mockito.verify(hotelService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenGetNotExistedClient_ThenReturnError() throws Exception {
        Mockito.when(hotelService.findById(500L)).thenThrow(new EntityNotFoundException("hotel with id 500 not found"));

        var response = mockMvc.perform(get("/api/v1/hotel/500")).andExpect(status().isNotFound()).andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/hotel_by_id_not_found_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).findById(500L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);


    }
}
