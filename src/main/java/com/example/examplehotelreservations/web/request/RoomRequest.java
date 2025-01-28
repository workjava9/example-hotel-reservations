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

import static com.example.examplehotelreservations.web.model.StringMessage.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotBlank(message = ERROR_NAME_MANDATORY)
    private String name;

    @NotBlank(message = ERROR_DESCRIPTION_MANDATORY)
    private String description;

    @NotNull(message = ERROR_NUMBER_MANDATORY)
    @Min(value = 1, message = ERROR_MORE_ZERO)
    private Integer number;

    @NotNull(message = ERROR_COST_MANDATORY)
    @Positive(message =ERROR_COST_MORE_ZERO)
    private Double cost;

    @NotNull(message = ERROR_GUEST_LIMIT)
    @Min(value = 1, message = ERROR_GUEST_MORE_ZERO)
    private Integer guestsLimit;

    private List<Timestamp> unavailableDates;

    @NotNull(message = ERROR_HOTEL_ID)
    private Long hotelId;

}
