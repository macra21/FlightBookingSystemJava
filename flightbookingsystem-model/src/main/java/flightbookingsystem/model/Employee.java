package flightbookingsystem.model;

/**
 * Represents an Employee entity within the application domain.
 */
public class Employee implements IEntity<Integer> {
    private Integer id;
    private String username;
    private String password;
    private String name;

    /**
     * Constructs a fully initialized Employee object.
     *
     * @param id       the unique identifier of the employee
     * @param username the employee's login username
     * @param password the employee's login password
     * @param name     the full name of the employee
     */
    public Employee(Integer id, String username, String password, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the employee.
     * @return the employee ID
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the employee.
     * @param id the new employee ID
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the employee's username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the employee's username.
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the employee's password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee's password.
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the employee's full name.
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee's full name.
     * @param name the new full name
     */
    public void setName(String name) {
        this.name = name;
    }
}