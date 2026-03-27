package com.org.FlightBookingSystem.validators;

import com.org.FlightBookingSystem.domain.Booking;
import com.org.FlightBookingSystem.exceptions.ValidationException;

public class BookingValidator implements IValidator<Booking> {
    public static void validate(Booking booking) {
        StringBuilder errors = new StringBuilder();

        if (booking.getId() != null && booking.getId() < 0){
            errors.append("Booking ID should be greater than or equal to 0.\n");
        }
        if (booking.getFlight() == null){
            errors.append("Booking should have an assigned flight.\n");
        }
        if (booking.getNumberOfSeats() <= 0){
            errors.append("Number of seats should be greater than 0.\n");
        }
        if (booking.getTouristNames().isEmpty()){
            errors.append("Tourist names should be set.\n");
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors.toString());
        }
    }
}
