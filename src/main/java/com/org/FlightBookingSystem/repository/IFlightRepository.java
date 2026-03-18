package com.org.FlightBookingSystem.repository;

import com.org.FlightBookingSystem.domain.Flight;

import java.time.LocalDateTime;

/**
 * Specialized repository interface for Flight related operations.
 */
public interface IFlightRepository extends IRepository<Integer, Flight> {

    /**
     * Retrieves flights by destination and departure date.
     *
     * @param destination the arrival airport name
     * @param date        the scheduled departure date
     * @return a collection of matching flights
     */
    Iterable<Flight> findByDestinationAndDepartureDate(String destination, LocalDateTime date);
}