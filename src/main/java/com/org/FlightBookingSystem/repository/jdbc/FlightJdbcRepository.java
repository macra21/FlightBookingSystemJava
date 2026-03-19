package com.org.FlightBookingSystem.repository.jdbc;

import com.org.FlightBookingSystem.domain.Flight;
import com.org.FlightBookingSystem.exceptions.RepositoryException;
import com.org.FlightBookingSystem.repository.IFlightRepository;
import com.org.FlightBookingSystem.utils.db.DBConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link IFlightRepository}.
 * This class handles all database related operations from CRUD to custom queries
 * for the {@link Flight} entity.
 */
public class FlightJdbcRepository implements IFlightRepository {
    /**
     * Saves a new {@link Flight} to the database and assigns it an auto generated id.
     *
     * @param entity the entity to persist
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void save(Flight entity) {
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "INSERT INTO flights (departure_airport, arrival_airport, departure_time, arrival_time, available_seats) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, entity.getDepartureAirport());
                preparedStatement.setString(2, entity.getArrivalAirport());
                preparedStatement.setObject(3, entity.getDepartureTime());
                preparedStatement.setObject(4, entity.getArrivalTime());
                preparedStatement.setInt(5, entity.getAvailableSeats());
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                    } else {
                        System.err.println("Saving flight failed, no ID obtained.");
                        throw new RepositoryException("Saving flight failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to save flight: " + e.getMessage());
            throw new RepositoryException("Failed to save flight: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }

    /**
     * Finds an {@link Flight} based on its id.
     *
     * @param id the ID of the entity
     * @return the {@link Flight} if found, or null otherwise
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public Flight findOne(Integer id) {
        Connection connection = null;
        Flight flight = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT * FROM flights WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        flight = parseFromResultSet(resultSet);
                    }
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to find Flight: " + e.getMessage());
            throw new RepositoryException("Failed to find Flight: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
        return flight;
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
    @Override
    public Iterable<Flight> findAll() {
        Connection connection = null;
        List<Flight> flights = new ArrayList<>();
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT * FROM flights";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    flights.add(parseFromResultSet(resultSet));
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to find all flights: " + e.getMessage());
            throw new RepositoryException("Failed to find all flights: " + e.getMessage(), e);
        }  finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
        return flights;
    }

    /**
     * Updates an existing {@link Flight} based on its id.
     *
     * @param entity the entity with updated information
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void update(Flight entity) {
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "UPDATE flights SET " +
                    "departure_airport = ?, " +
                    "arrival_airport = ?, " +
                    "departure_time = ?, " +
                    "arrival_time = ?, " +
                    "available_seats = ? " +
                    "WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getDepartureAirport());
                preparedStatement.setString(2, entity.getArrivalAirport());
                preparedStatement.setObject(3, entity.getDepartureTime());
                preparedStatement.setObject(4, entity.getArrivalTime());
                preparedStatement.setInt(5, entity.getAvailableSeats());
                preparedStatement.setInt(6, entity.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.err.println("Failed to update flight, no rows affected.");
                    throw new RepositoryException("Failed to update flight, no rows affected.");
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to update flight: " + e.getMessage());
            throw new RepositoryException("Failed to update flight: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes an {@link Flight} based on its id.
     *
     * @param id the ID of the entity to remove
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void delete(Integer id) {
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "DELETE FROM flights WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.err.println("Failed to delete flight, no rows affected.");
                    throw new RepositoryException("Failed to delete flight, no rows affected.");
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to delete flight: " + e.getMessage());
            throw new RepositoryException("Failed to delete flight: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }

    /**
     * Finds all flights based on their destination and departure date
     * @param destination the arrival airport name
     * @param date        the scheduled departure date
     * @return an {@link Iterable} collection of the respective flights
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Iterable<Flight> findByDestinationAndDepartureDate(String destination, LocalDateTime date) {
        Connection connection = null;
        List<Flight> flights = new ArrayList<>();
        try {
            connection = DBConnectionManager.getConnection();
            // Maybe set departure_time before date????t.b.d.
            String sql = "SELECT * FROM flights WHERE arrival_airport = ? AND DATE(departure_time) = DATE(?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, destination);
                preparedStatement.setObject(2, date);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        flights.add(parseFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to find Flight: " + e.getMessage());
            throw new RepositoryException("Failed to find Flight: " + e.getMessage(), e);
        } finally {
            try {
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e) {
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
        return flights;
    }

    /**
     * Helper method that maps a single row from the {@link ResultSet} into a {@link Flight} object.
     *
     * @param resultSet the ResultSet pointing to the current row
     * @return the new {@link Flight}
     * @throws SQLException if a database error occurs.
     */
    private Flight parseFromResultSet(ResultSet resultSet) throws SQLException {
        return new Flight(
                resultSet.getInt("id"),
                resultSet.getString("departure_airport"),
                resultSet.getString("arrival_airport"),
                resultSet.getObject("departure_time", LocalDateTime.class),
                resultSet.getObject("arrival_time", LocalDateTime.class),
                resultSet.getInt("available_seats")
        );
    }
}
