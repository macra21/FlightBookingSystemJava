package com.org.FlightBookingSystem.service;

import com.org.FlightBookingSystem.domain.Flight;
import com.org.FlightBookingSystem.exceptions.RepositoryException;
import com.org.FlightBookingSystem.exceptions.ValidationException;
import com.org.FlightBookingSystem.repository.IFlightRepository;
import com.org.FlightBookingSystem.validators.FlightValidator;

import java.time.LocalDateTime;

/**
 * Service class for {@link Flight} entities.
 * <p>
 *     Includes CRUD operations, as well as complex filtering operations.
 * </p>
 */
public class FlightService {
    private final IFlightRepository flightRepository;

    /**
     * Constructs the service using an interface for easy swapping between
     * persistence types.
     * @param flightRepository a repository that extends {@link IFlightRepository}
     */
    public FlightService(IFlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Validates and saves a new {@link Flight} to the database.
     *
     * @param flight the entity to persist
     * @throws RepositoryException if a database error occurs.
     * @throws ValidationException if the provided flight data violates validation constraints
     */
    public void save(Flight flight) {
        FlightValidator.validate(flight);
        flightRepository.save(flight);
    }

    /**
     * Finds a {@link Flight} based on its id.
     *
     * @param id the ID of the entity
     * @return the {@link Flight} if found, or null otherwise
     * @throws RepositoryException if a database error occurs.
     */
    public Flight findOne(int id) {
        return flightRepository.findOne(id);
    }

    /**
     * Retrieves all the flights from the database.
     * <p>
     *     <strong>WARNING:</strong>
     *     Use this function carefully, because there can be
     *     lots of entities in the database.
     * </p>
     * @return an {@link Iterable} collection of all flights
     * @throws RepositoryException if a database error occurs
     */
    public Iterable<Flight> findAll() {
        return flightRepository.findAll();
    }

    /**
     * Updates an existing {@link Flight} based on its id.
     *
     * @param flight the entity with updated information
     * @throws RepositoryException if a database error occurs.
     * @throws ValidationException if the provided flight data violates validation constraints
     */
    public void update(Flight flight) {
        FlightValidator.validate(flight);
        flightRepository.update(flight);
    }

    /**
     * Deletes a {@link Flight} based on its id.
     *
     * @param id the ID of the entity to remove
     * @throws RepositoryException if a database error occurs.
     */
    public void delete(int id) {
        flightRepository.delete(id);
    }

    /**
     * Retrieves flights by destination and departure date.
     * <p>
     *     Filters the system to find flights matching the exact expected date
     *     and landing at the selected airport.
     * </p>
     *
     * @param destination the arrival airport name
     * @param date        the scheduled departure date
     * @return a collection of matching {@link Flight} objects
     * @throws RepositoryException if a database error occurs
     */
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, LocalDateTime date) {
        return flightRepository.findByDestinationAndDepartureDate(destination, date);
    }
}
