package com.org.FlightBookingSystem.repository.jdbc;

import com.org.FlightBookingSystem.domain.Employee;
import com.org.FlightBookingSystem.exceptions.RepositoryException;
import com.org.FlightBookingSystem.repository.IEmployeeRepository;
import com.org.FlightBookingSystem.utils.db.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link IEmployeeRepository}.
 * This class handles all database related operations from CRUD to custom queries
 * for the {@link Employee} entity.
 */
public class EmployeeJdbcRepository implements IEmployeeRepository {
    /**
     * Saves a new {@link Employee} to the database and assigns it an auto generated id.
     *
     * @param entity the entity to persist
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void save(Employee entity) {
        Connection connection = null;
        try {
            connection = DBConnectionManager.getConnection();
            String sql = "INSERT INTO employees (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, entity.getUsername());
                preparedStatement.setString(2, entity.getPassword());
                preparedStatement.executeUpdate();
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        entity.setId(resultSet.getInt(1));
                    } else {
                        System.err.println("Saving employee failed, no ID obtained.");
                        throw new RepositoryException("Saving employee failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to save employee: " + e.getMessage());
            throw new RepositoryException("Failed to save employee: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }

    /**
     * Finds an {@link Employee} based on their id.
     *
     * @param id the ID of the entity
     * @return the {@link Employee} if found, or null otherwise
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public Employee findOne(Integer id) {
        Connection connection = null;
        Employee employee = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT * FROM employees WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        employee =  new Employee(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password")
                                );
                    }
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to find employee: " + e.getMessage());
            throw new RepositoryException("Failed to find employee: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
        return employee;
    }

    /**
     * Retrieves all the employees from the database.
     * <p>
     *     <strong>WARNING:</strong>
     *     Use this function carefully, because there can be
     *     lots of entities in the database.
     * </p>
     * @return an {@link Iterable} collection of all employees
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Iterable<Employee> findAll() {
        Connection connection = null;
        List<Employee> employeeList = new ArrayList<>();
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT * FROM employees";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    employeeList.add(new Employee(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    ));
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to find all employees: " + e.getMessage());
            throw new RepositoryException("Failed to find all employees: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
        return employeeList;
    }

    /**
     * Updates an existing {@link Employee} based on their id.
     *
     * @param entity the entity with updated information
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void update(Employee entity) {
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "UPDATE employees SET username = ?, " +
                    "password = ? " +
                    "WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, entity.getUsername());
                preparedStatement.setString(2, entity.getPassword());
                preparedStatement.setInt(3, entity.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0){
                    System.err.println("Failed to update employee, no rows affected.");
                    throw new RepositoryException("Failed to update employee, no rows affected.");
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to update employee: " + e.getMessage());
            throw new RepositoryException("Failed to update employee: " + e.getMessage(), e);
        } finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes an {@link Employee} based on their id.
     *
     * @param id the ID of the entity to remove
     * @throws RepositoryException if a database error occurs.
     */
    @Override
    public void delete(Integer id) {
        Connection connection = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "DELETE FROM employees WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0){
                    System.err.println("Deleting employee failed, no rows affected.");
                    throw new RepositoryException("Deleting employee failed, no rows affected.");
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to delete employee: " + e.getMessage());
            throw new RepositoryException("Failed to delete employee: " + e.getMessage(), e);
        }  finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
    }

    /**
     * Finds an {@link Employee} based on their username and password
     * <p>
     *     This is used in the AuthentificationService for login.
     * </p>
     * @param username the account username
     * @param password the account password
     * @return the {@link Employee} if credentials match, or null otherwise
     * @throws RepositoryException if a database error occurs
     */
    @Override
    public Employee findByUsernameAndPassword(String username, String password) {
        Connection connection = null;
        Employee employee = null;
        try{
            connection = DBConnectionManager.getConnection();
            String sql = "SELECT * FROM employees WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if (resultSet.next()){
                        employee = new Employee(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password")
                        );
                    }
                }
            }
        } catch (SQLException e){
            System.err.println("Failed to find employee: " + e.getMessage());
            throw new RepositoryException("Failed to find employee: " + e.getMessage(), e);
        }  finally {
            try{
                DBConnectionManager.releaseConnection(connection);
            } catch (SQLException e){
                System.err.println("Failed to release connection: " + e.getMessage());
            }
        }
        return employee;
    }
}
