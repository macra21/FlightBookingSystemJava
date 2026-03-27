package com.org.FlightBookingSystem.service;

import com.org.FlightBookingSystem.domain.Employee;
import com.org.FlightBookingSystem.exceptions.RepositoryException;
import com.org.FlightBookingSystem.exceptions.ServiceException;
import com.org.FlightBookingSystem.repository.IEmployeeRepository;
import com.org.FlightBookingSystem.utils.Encryption;
import com.org.FlightBookingSystem.validators.EmployeeValidator;
import com.org.FlightBookingSystem.exceptions.ValidationException;

import java.util.Objects;

/**
 * Service class for {@link Employee} entities.
 * <p>
 *     Includes CRUD operations, as well as more complex ones.
 * </p>
 */
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;

    /**
     * Constructs the service using an interface for easy swapping between
     * persistence types.
     * @param employeeRepository a repository that extends {@link IEmployeeRepository}
     */
    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Validates and saves a new {@link Employee} to the database.
     * <p>
     *     <strong>WARNING:</strong>
     *     Hash the users password before calling this function, because
     *     this function does NOT hash the password so use it carefully.
     * </p>
     * @param employee the entity to persist
     * @throws RepositoryException if a persistence error occurs.
     * @throws ValidationException if the given employee object is invalid
     */
    public void save(Employee employee) {
        EmployeeValidator.validate(employee);
        employeeRepository.save(employee);
    }

    /**
     * Finds an {@link Employee} based on their id.
     *
     * @param id the ID of the entity
     * @return the {@link Employee} if found, or null otherwise
     * @throws RepositoryException if a database error occurs.
     */
    public Employee findOne(int id) {
        return employeeRepository.findOne(id);
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
    public Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Updates an existing {@link Employee} based on their id.
     * <p>
     *     <strong>WARNING:</strong>
     *     Hash the users password before calling this function, because
     *     this function does NOT hash the password so use it carefully.
     * </p>
     * @param employee the entity with updated information
     * @throws RepositoryException if a database error occurs.
     * @throws ValidationException if the given employee object is invalid
     */
    public void update(Employee employee) {
        EmployeeValidator.validate(employee);
        employeeRepository.update(employee);
    }

    /**
     * Deletes an {@link Employee} based on their id.
     *
     * @param id the ID of the entity to remove
     * @throws RepositoryException if a database error occurs.
     */
    public void delete(int id) {
        employeeRepository.delete(id);
    }

    /**
     * Authenticates an {@link Employee} based on their username and password.
     * <p>
     *     This method retrieves the employee from the database and verifies if the
     *     provided plain-text password matches the stored SHA-256 hash.
     * </p>
     *
     * @param username the employee's login username
     * @param password the employee's plain-text password
     * @return the authenticated {@link Employee} object
     * @throws ServiceException if the employee is not found or the password is invalid
     * @throws RepositoryException if a database error occurs
     */
    public Employee login(String username, String password) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new ServiceException("Employee not found");
        }
        if (!Objects.equals(employee.getPassword(), Encryption.SHA256OneWayHash(password))) {
            throw new ServiceException("Invalid password");
        }
        return employee;
    }

    /**
     * Validates and registers a new {@link Employee}.
     * <p>
     *     Unlike the standard {@link #save(Employee)} method, this function
     *     automatically hashes the employee's plain-text password using SHA-256
     *     before persisting the entity to the database.
     * </p>
     *
     * @param employee the entity to register, containing a plain-text password
     * @throws ValidationException if the provided employee data violates validation constraints
     * @throws RepositoryException if a database error occurs
     */
    public void register(Employee employee) {
        EmployeeValidator.validate(employee);
        employee.setPassword(Encryption.SHA256OneWayHash(employee.getPassword()));
        employeeRepository.save(employee);
    }
}
