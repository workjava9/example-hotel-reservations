package com.example.examplehotelreservations.web.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StringMessage {

    public static final String ERROR_NAME = "Name must not be empty";

    public static final String ERROR_HEADLINE  = "Headline must not be empty";

    public static final String ERROR_CITY = "City must not be empty";

    public static final String ERROR_ADDRESS = "Address must not be empty";

    public static final String ERROR_CENTER_DISTANCE_NULL = "Center distance must not be null";

    public static final String ERROR_CENTER_DISTANCE_NOT_NULL = "Center distance must be positive";

    public static final String ERROR_DESCRIPTION_MANDATORY = "Description is mandatory";

    public static final String ERROR_NUMBER_MANDATORY = "Number is mandatory";

    public static final String ERROR_MORE_ZERO = "Number must be greater than 0";

    public static final String ERROR_COST_MANDATORY = "Cost is mandatory";

    public static final String ERROR_COST_MORE_ZERO = "Cost must be greater than 0";

    public static final String ERROR_GUEST_LIMIT = "Guests limit is mandatory";

    public static final String ERROR_GUEST_MORE_ZERO = "Guests limit must be greater than 0";

    public static final String ERROR_HOTEL_ID =  "Hotel ID is mandatory";

    public static final String ERROR_NAME_MANDATORY =  "Name is mandatory";

}
