package com.org.FlightBookingSystem.repository;


import com.org.FlightBookingSystem.domain.Employee;

/**
 * Specialized repository interface for Employee related operations.
 */
public interface IEmployeeRepository extends IRepository<Integer, Employee> {
    /**
     * Retrieves an employee based on login credentials.
     *
     * @param username the account username
     * @param password the account password
     * @return the Employee object if credentials match, null otherwise
     */
    Employee findByUsernameAndPassword(String username, String password);
}