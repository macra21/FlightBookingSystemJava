package com.org.FlightBookingSystem.domain;

import java.util.List;

/**
 * Represents Booking object within the application domain.
 */
public class Booking implements IEntity<Integer> {
    private Integer id;
    private Flight flight;
    private Integer numberOfSeats;
    private List<String> touristNames;

    /**
     * Constructs a fully initialized Booking object.
     * <p>
     *     This constructor is typically used when retrieving the object from
     *     the database and already has an assigned id.
     * </p>
     *
     * @param id             the unique identifier of the booking
     * @param flight         the flight associated with this booking
     * @param numberOfSeats  the number of seats reserved
     * @param touristNames   the list of names for all tourists in this booking
     */
    public Booking(Integer id, Flight flight, Integer numberOfSeats, List<String> touristNames) {
        this.id = id;
        this.flight = flight;
        this.numberOfSeats = numberOfSeats;
        this.touristNames = touristNames;
    }

    /**
     * Constructs a Booking object without an id.
     * <p>
     *     This constructor is typically used before saving the object to
     *     the database and does not have an assigned id.
     * </p>
     *
     * @param flight         the flight associated with this booking
     * @param numberOfSeats  the number of seats reserved
     * @param touristNames   the list of names for all tourists in this booking
     */
    public Booking(Flight flight, Integer numberOfSeats, List<String> touristNames) {
        this.flight = flight;
        this.numberOfSeats = numberOfSeats;
        this.touristNames = touristNames;
    }

    /**
     * Gets the unique identifier of the booking.
     *
     * @return the booking ID
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the booking.
     *
     * @param id the new booking ID
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the associated flight.
     *
     * @return the flight object
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * Sets the associated flight.
     *
     * @param flight the new flight object
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * Gets the number of seats reserved in this booking.
     *
     * @return the number of seats
     */
    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    /**
     * Sets the number of seats reserved in this booking.
     *
     * @param numberOfSeats the new number of seats
     */
    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    /**
     * Gets the list of tourists included in this booking.
     *
     * @return a list of names
     */
    public List<String> getTouristNames() {
        return touristNames;
    }

    /**
     * Sets the list of tourists included in this booking.
     *
     * @param touristNames the new list of names
     */
    public void setTouristNames(List<String> touristNames) {
        this.touristNames = touristNames;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", flight=" + flight +
                ", numberOfSeats=" + numberOfSeats +
                ", touristNames=" + touristNames +
                '}';
    }
}