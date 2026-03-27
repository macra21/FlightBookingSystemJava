package com.org.FlightBookingSystem.validators;


import com.org.FlightBookingSystem.domain.Flight;
import com.org.FlightBookingSystem.exceptions.ValidationException;

public class FlightValidator implements IValidator<Flight> {
    public static void validate(Flight flight){
        StringBuilder errors = new StringBuilder();
        if (flight.getId() != null && flight.getId() < 0){
            errors.append("Flight ID should be greater than or equal to 0.\n");
        }
        if (flight.getAvailableSeats() < 0){
            errors.append("Available seats should be greater than or equal to 0.\n");
        }
        if (flight.getArrivalTime().isBefore(flight.getDepartureTime())){
            errors.append("Arrival time should be greater than or equal to departure time.\n");
        }

        if (!errors.isEmpty()){
            throw new ValidationException(errors.toString());
        }
    }

}
