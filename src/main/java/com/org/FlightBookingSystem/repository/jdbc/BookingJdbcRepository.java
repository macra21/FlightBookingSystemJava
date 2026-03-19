package com.org.FlightBookingSystem.repository.jdbc;

import com.org.FlightBookingSystem.domain.Booking;
import com.org.FlightBookingSystem.domain.Flight;
import com.org.FlightBookingSystem.exceptions.RepositoryException;
import com.org.FlightBookingSystem.repository.IBookingRepository;
import com.org.FlightBookingSystem.utils.db.DBConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JDBC implementation of {@link IBookingRepository}.
 * This class handles all database related operations from CRUD to custom queries
 * for the {@link Booking} entity.
 */
public class BookingJdbcRepository implements IBookingRepository {

    private static final Logger logger = LogManager.getLogger(BookingJdbcRepository.class);

    /**
     * Saves a new {@link Booking} to the database and assigns it an auto generated id.
     *
     * @param entity the entity to persist
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void save(Booking entity) {
        Connection connection = null;
        logger.traceEntry("Saving booking: " + entity.toString());
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "INSERT INTO bookings (flight_id, number_of_seats, tourist_names) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, entity.getFlight().getId());
                preparedStatement.setInt(2, entity.getNumberOfSeats());
                String touristNames = String.join(", ", entity.getTouristNames());
                preparedStatement.setString(3, touristNames);
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                        logger.traceExit("Saved booking: " +  entity.toString());
                    } else {
                        RepositoryException re = new RepositoryException("Saving booking failed, no ID obtained.");
                        logger.throwing(re);
                        throw re;
                    }
                }
            }
        } catch (SQLException e){
            logger.catching(e);
            RepositoryException re = new RepositoryException("Failed to save booking: " + e.getMessage(), e);
            logger.throwing(re);
            throw re;
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            }  catch (SQLException e){
                logger.catching(e);
            }
        }
    }

    /**
     * Finds an {@link Booking} based on their id.
     *
     * @param id the ID of the entity
     * @return the {@link Booking} if found, or null otherwise
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public Booking findOne(Integer id) {
        logger.traceEntry("Finding booking with id=" + id);
        Connection connection = null;
        Booking booking = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT b.*, f.departure_airport, f.arrival_airport, f.departure_time, f.arrival_time, f.available_seats " +
                    "FROM bookings b " +
                    "JOIN flights f ON b.flight_id = f.id " +
                    "WHERE b.id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        booking = parseFromResultSet(resultSet);
                    }
                }
            }
        } catch (SQLException e){
            logger.catching(e);
            RepositoryException re = new RepositoryException("Failed to find booking: " + e.getMessage(), e);
            logger.throwing(re);
            throw re;
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            }  catch (SQLException e){
                logger.catching(e);
            }
        }
        if (booking == null) {
            logger.traceExit("Booking with id=" + id + " NOT found.");
        } else {
            logger.traceExit("Booking with id=" + id + " found.");
        }
        return booking;
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
    @Override
    public Iterable<Booking> findAll() {
        logger.traceEntry("Finding all bookings.");
        Connection connection = null;
        List<Booking> bookings = new ArrayList<>();
        try {
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT b.*, f.departure_airport, f.arrival_airport, f.departure_time, f.arrival_time, f.available_seats " +
                    "FROM bookings b " +
                    "JOIN flights f ON b.flight_id = f.id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(parseFromResultSet(resultSet));
                }
            }
        } catch (SQLException e){
            logger.catching(e);
            RepositoryException re = new RepositoryException("Failed to find all bookings: " + e.getMessage(), e);
            logger.throwing(re);
            throw re;
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            }  catch (SQLException e){
                logger.catching(e);
            }
        }
        logger.traceExit("Returned found bookings.");
        return bookings;
    }

    /**
     * Updates an existing {@link Booking} based on its id.
     *
     * @param entity the entity with updated information
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void update(Booking entity) {
        logger.traceEntry("Updating booking: " + entity.toString());
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "UPDATE bookings SET " +
                    "flight_id = ?, " +
                    "number_of_seats = ?, " +
                    "tourist_names = ? " +
                    "WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, entity.getFlight().getId());
                preparedStatement.setInt(2, entity.getNumberOfSeats());
                String touristNames = String.join(", ", entity.getTouristNames());
                preparedStatement.setString(3, touristNames);
                preparedStatement.setInt(4, entity.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    RepositoryException re = new RepositoryException("Failed to update booking, no rows affected.");
                    logger.throwing(re);
                    throw re;
                }
                logger.traceExit("Updated booking: " + entity.toString());
            }
        } catch (SQLException e){
            logger.catching(e);
            RepositoryException re =  new RepositoryException("Failed to update booking: " + e.getMessage(), e);
            logger.throwing(re);
            throw re;
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            }  catch (SQLException e){
                logger.catching(e);
            }
        }
    }

    /**
     * Deletes an {@link Booking} based on its id.
     *
     * @param id the ID of the entity to remove
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting booking with id=" + id);
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "DELETE FROM bookings WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    RepositoryException re = new RepositoryException("Failed to delete booking, no rows affected.");
                    logger.throwing(re);
                    throw re;
                }
                logger.traceExit("Deleted booking: " + id);
            }
        } catch (SQLException e){
            logger.catching(e);
            RepositoryException re = new RepositoryException("Failed to delete booking: " + e.getMessage(), e);
            logger.throwing(re);
            throw re;
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                logger.catching(e);
            }
        }
    }

    /**
     * Helper method that maps a single row from the {@link ResultSet} into a {@link Booking} object.
     *
     * @param resultSet the ResultSet pointing to the current row
     * @return the new {@link Booking}
     * @throws SQLException if a database error occurs.
     */
    private Booking parseFromResultSet(ResultSet resultSet) throws SQLException {
        Flight flight = new Flight(
                resultSet.getInt("flight_id"),
                resultSet.getString("departure_airport"),
                resultSet.getString("arrival_airport"),
                resultSet.getObject("departure_time", LocalDateTime.class),
                resultSet.getObject("arrival_time", LocalDateTime.class),
                resultSet.getInt("available_seats")
        );
        String touristNames = resultSet.getString("tourist_names");
        List<String> names = new ArrayList<>(Arrays.asList(touristNames.split(", ")));
        return new Booking(
                resultSet.getInt("id"),
                flight,
                resultSet.getInt("number_of_seats"),
                names
        );
    }
}
