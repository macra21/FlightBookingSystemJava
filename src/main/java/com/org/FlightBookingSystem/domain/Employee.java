package com.org.FlightBookingSystem.domain;

/**
 * Represents an Employee entity within the application domain.
 */
public class Employee implements IEntity<Integer> {
    private Integer id;
    private String username;
    private String password;

    /**
     * Constructs a fully initialized Employee object.
     * <p>
     *     This constructor is typically used when retrieving the object from
     *     the database and already has an assigned id.
     * </p>
     *
     * @param id       the unique identifier of the employee
     * @param username the employee's login username
     * @param password the employee's login password
     */
    public Employee(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs an Employee object without an id.
     * <p>
     *     This constructor is typically used before saving the object to
     *     the database and does not have an assigned id.
     * </p>
     *
     * @param username the employee's login username
     * @param password the employee's login password
     */
    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * Gets the unique identifier of the employee.
     *
     * @return the employee ID
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the employee.
     *
     * @param id the new employee ID
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the employee's username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the employee's username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the employee's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}