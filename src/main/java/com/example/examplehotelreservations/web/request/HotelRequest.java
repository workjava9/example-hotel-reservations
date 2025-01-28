package com.example.examplehotelreservations.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.examplehotelreservations.web.model.StringMessage.*;
import static jakarta.servlet.RequestDispatcher.ERROR_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

    @NotBlank(message = ERROR_NAME)
    private String name;

    @NotBlank(message = ERROR_HEADLINE)
    private String headline;

    @NotBlank(message = ERROR_CITY)
    private String city;

    @NotBlank(message = ERROR_ADDRESS)
    private String address;

    @NotNull(message = ERROR_CENTER_DISTANCE_NULL)
    @Positive(message = ERROR_CENTER_DISTANCE_NOT_NULL)
    private Double centerDistance;
}