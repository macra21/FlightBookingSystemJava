package flightbookingsystem.repository.db;

import flightbookingsystem.model.Flight;
import flightbookingsystem.repository.IFlightRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for the Flight Repository using SQLite database.
 * This class handles all CRUD operations and specialized queries for Flight entities.
 */
public class FlightDBRepository implements IFlightRepository {

    /**
     * Persists a new Flight entity into the database.
     * @param entity the Flight object to be saved
     */
    @Override
    public void save(Flight entity) {
        String sql = "INSERT INTO flights (departure_airport, arrival_airport, departure_time, arrival_time, available_seats) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getDepartureAirport());
            ps.setString(2, entity.getArrivalAirport());
            ps.setString(3, entity.getDepartureTime().toString());
            ps.setString(4, entity.getArrivalTime().toString());
            ps.setInt(5, entity.getAvailableSeats());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database Error (save): " + e.getMessage());
        }
    }

    /**
     * Updates an existing Flight entity in the database based on its ID.
     * @param entity the Flight object containing updated information
     */
    @Override
    public void update(Flight entity) {
        String sql = "UPDATE flights SET departure_airport=?, arrival_airport=?, departure_time=?, arrival_time=?, available_seats=? WHERE id=?";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getDepartureAirport());
            ps.setString(2, entity.getArrivalAirport());
            ps.setString(3, entity.getDepartureTime().toString());
            ps.setString(4, entity.getArrivalTime().toString());
            ps.setInt(5, entity.getAvailableSeats());
            ps.setInt(6, entity.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0){
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Database Error (update): " + e.getMessage());
        }
    }

    /**
     * Removes a Flight record from the database.
     * @param id the unique identifier of the flight to delete
     */
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM flights WHERE id = ?";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0){
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Database Error (delete): " + e.getMessage());
        }
    }

    /**
     * Finds a specific Flight by its unique ID.
     * @param id the identifier to search for
     * @return the Flight object if found, null otherwise
     */
    @Override
    public Flight findOne(Integer id) {
        String sql = "SELECT * FROM flights WHERE id = ?";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractFlight(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error (findOne): " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all Flight records currently stored in the database.
     * @return an Iterable collection of all flights
     */
    @Override
    public Iterable<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                flights.add(extractFlight(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (findAll): " + e.getMessage());
        }
        return flights;
    }

    /**
     * Searches for flights destined for a specific airport on a specific date.
     * @param destination the arrival airport
     * @param date the date of departure
     * @return a list of flights matching the criteria
     */
    @Override
    public Iterable<Flight> findByDestinationAndDate(String destination, LocalDateTime date) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE arrival_airport = ? AND departure_time LIKE ?";

        try (Connection con = JdbcUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, destination);
            ps.setString(2, date.toLocalDate().toString() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    flights.add(extractFlight(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error (findByDestinationAndDate): " + e.getMessage());
        }
        return flights;
    }

    /**
     * Helper method to map a single row from the ResultSet into a Flight domain object.
     * @param rs the ResultSet pointing to a valid row
     * @return a newly created Flight object
     * @throws SQLException if column names are missing or data types mismatch
     */
    private Flight extractFlight(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String departure = rs.getString("departure_airport");
        String arrival = rs.getString("arrival_airport");
        LocalDateTime departureTime = LocalDateTime.parse(rs.getString("departure_time"));
        LocalDateTime arrivalTime = LocalDateTime.parse(rs.getString("arrival_time"));
        int availableSeats = rs.getInt("available_seats");

        return new Flight(id, departure, arrival, departureTime, arrivalTime, availableSeats);
    }
}