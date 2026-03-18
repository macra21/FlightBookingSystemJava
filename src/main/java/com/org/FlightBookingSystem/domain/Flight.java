package com.org.FlightBookingSystem.domain;

import java.time.LocalDateTime;

/**
 * Represents the Flight entity within the application domain.
 */
public class Flight implements IEntity<Integer> {
    private Integer id;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;

    /**
     * Constructs a fully initialized Flight object.
     * <p>
     *     This constructor is typically used when retrieving the object from
     *     the database and already has an assigned id.
     * </p>
     *
     * @param id the unique identifier of the flight
     * @param departureAirport the departure airport
     * @param arrivalAirport   the destination airport
     * @param departureTime    the scheduled date and time of departure
     * @param arrivalTime      the scheduled date and time of arrival
     * @param availableSeats   the number of seats currently available for booking
     */
    public Flight(Integer id, String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer availableSeats) {
        this.id = id;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
    }

    /**
     * Constructs a Flight object without an id.
     * <p>
     *     This constructor is typically used before saving the object to
     *     the database and does not have an assigned id.
     * </p>
     *
     * @param departureAirport the departure airport
     * @param arrivalAirport   the destination airport
     * @param departureTime    the scheduled date and time of departure
     * @param arrivalTime      the scheduled date and time of arrival
     * @param availableSeats   the number of seats currently available for booking
     */
    public Flight(String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer availableSeats) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
    }



    /**
     * Gets the unique identifier of the flight.
     *
     * @return the flight ID
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the flight.
     *
     * @param id the new flight ID
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the departure airport.
     *
     * @return the departure airport name
     */
    public String getDepartureAirport() {
        return departureAirport;
    }

    /**
     * Sets the departure airport of the flight.
     *
     * @param departureAirport the new departure airport
     */
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    /**
     * Gets the arrival airport.
     *
     * @return the arrival airport name
     */
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * Sets the arrival airport of the flight.
     *
     * @param arrivalAirport the new arrival airport
     */
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    /**
     * Gets the scheduled departure time.
     *
     * @return the departure date and time
     */
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Sets the scheduled departure time of the flight.
     *
     * @param departureTime the new departure date and time
     */
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Gets the scheduled arrival time.
     *
     * @return the arrival date and time
     */
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the scheduled arrival time of the flight.
     *
     * @param arrivalTime the new arrival date and time
     */
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Gets the number of seats available for this flight.
     *
     * @return the available seats count
     */
    public Integer getAvailableSeats() {
        return availableSeats;
    }

    /**
     * Sets the number of available seats for this flight.
     *
     * @param availableSeats the new available seats count
     */
    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}