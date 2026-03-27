package com.org.FlightBookingSystem.service;

import com.org.FlightBookingSystem.domain.Booking;
import com.org.FlightBookingSystem.exceptions.RepositoryException;
import com.org.FlightBookingSystem.exceptions.ValidationException;
import com.org.FlightBookingSystem.repository.IBookingRepository;
import com.org.FlightBookingSystem.validators.BookingValidator;

/**
 * Service class for {@link Booking} entities.
 * <p>
 *     Includes CRUD operations for managing flight bookings.
 * </p>
 */
public class BookingService {
    private final IBookingRepository bookingRepository;

    /**
     * Constructs the service using an interface for easy swapping between
     * persistence types.
     * @param bookingRepository a repository that extends {@link IBookingRepository}
     */
    public BookingService(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Validates and saves a new {@link Booking} to the database.
     *
     * @param booking the entity to persist
     * @throws RepositoryException if a persistence error occurs.
     * @throws ValidationException if the given booking object is invalid
     */
    public void save(Booking booking) {
        BookingValidator.validate(booking);
        bookingRepository.save(booking);
    }

    /**
     * Finds a {@link Booking} based on its id.
     *
     * @param id the ID of the entity
     * @return the {@link Booking} if found, or null otherwise
     * @throws RepositoryException if a database error occurs.
     */
    public Booking findOne(int id) {
        return bookingRepository.findOne(id);
    }

    /**
     * Retrieves all the bookings from the database.
     * <p>
     *     <strong>WARNING:</strong>
     *     Use this function carefully, because there can be
     *     lots of entities in the database.
     * </p>
     * @return an {@link Iterable} collection of all bookings
     * @throws RepositoryException if a database error occurs
     */
    public Iterable<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * Updates an existing {@link Booking} based on its id.
     *
     * @param booking the entity with updated information
     * @throws RepositoryException if a database error occurs.
     * @throws ValidationException if the given booking object is invalid
     */
    public void update(Booking booking) {
        BookingValidator.validate(booking);
        bookingRepository.update(booking);
    }

    /**
     * Deletes a {@link Booking} based on its id.
     *
     * @param id the ID of the entity to remove
     * @throws RepositoryException if a database error occurs.
     */
    public void delete(int id) {
        bookingRepository.delete(id);
    }
}
