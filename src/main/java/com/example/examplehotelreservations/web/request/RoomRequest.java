package com.example.examplehotelreservations.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Number is mandatory")
    @Min(value = 1, message = "Number must be greater than 0")
    private Integer number;

    @NotNull(message = "Cost is mandatory")
    @Positive(message = "Cost must be greater than 0")
    private Double cost;

    @NotNull(message = "Guests limit is mandatory")
    @Min(value = 1, message = "Guests limit must be greater than 0")
    private Integer guestsLimit;

    private List<Timestamp> unavailableDates;

    @NotNull(message = "Hotel ID is mandatory")
    private Long hotelId;

}
